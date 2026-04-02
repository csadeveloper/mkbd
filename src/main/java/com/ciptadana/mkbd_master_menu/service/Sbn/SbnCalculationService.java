package com.ciptadana.mkbd_master_menu.service.Sbn;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnCalculationResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class SbnCalculationService {

    private static final int MATURITY_SCALE = 3;
    private static final int HAIRCUT_SCALE = 3;
    private static final BigDecimal DAYS_PER_YEAR = BigDecimal.valueOf(365);

    private static final BigDecimal MATURITY_THRESHOLD_SHORT = BigDecimal.valueOf(7);
    private static final BigDecimal MATURITY_THRESHOLD_MEDIUM = BigDecimal.valueOf(15);

    private static final BigDecimal HAIRCUT_SHORT = new BigDecimal("0.050");
    private static final BigDecimal HAIRCUT_MEDIUM = new BigDecimal("0.075");
    private static final BigDecimal HAIRCUT_LONG = new BigDecimal("0.100");

    private static final BigDecimal CONCENTRATION_THRESHOLD_RATE = new BigDecimal("0.20");

    public List<SbnCalculationResponse> calculateAll(List<SbnListResponse> sbnList,
                                                     LocalDate asOfDate,
                                                     BigDecimal lastEquity) {
        return sbnList.stream()
                .map(sbn -> calculate(sbn, asOfDate, lastEquity))
                .toList();
    }

    private SbnCalculationResponse calculate(SbnListResponse sbn,
                                             LocalDate asOfDate,
                                             BigDecimal lastEquity) {
        BigDecimal remPeriod = calculateRemainingMaturityPeriod(asOfDate, sbn.getDueDate());
        BigDecimal haircutPct = determineHaircutPercentage(remPeriod);
        BigDecimal haircutVal = calculateHaircutValue(sbn.getMarketValue(), haircutPct);
        BigDecimal afterHaircut = calculateValueAfterHaircut(sbn.getMarketValue(), haircutVal);
        BigDecimal concernRisk = calculateConcentrationRisk(sbn.getMarketValue(), lastEquity);

        return SbnCalculationResponse.builder()
                .id(sbn.getId())
                .recdate(sbn.getRecdate())
                .nshare(sbn.getNshare())
                .dueDate(sbn.getDueDate())
                .price(sbn.getPrice())
                .nominal(sbn.getNominal())
                .marketValue(sbn.getMarketValue())
                .affiliated(sbn.getAffiliated())
                .groupShare(sbn.getGroupShare())
                .acquisitionPrice(sbn.getAcquisitionPrice())
                .sisaMasaJatuhTempo(remPeriod)
                .persentaseHaircut(haircutPct.toPlainString())
                .nilaiHaircut(haircutVal)
                .nilaiSetelahHaircut(afterHaircut)
                .risikoKonsentrasi(concernRisk)
                .build();
    }

    BigDecimal calculateRemainingMaturityPeriod(LocalDate asOfDate, Date dueDate) {
        LocalDate maturityDate = dueDate.toInstant()
                .atZone(ZoneId.of("Asia/Jakarta"))
                .toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(asOfDate, maturityDate);
        return BigDecimal.valueOf(daysBetween)
                .divide(DAYS_PER_YEAR, MATURITY_SCALE, RoundingMode.HALF_UP);
    }

    BigDecimal determineHaircutPercentage(BigDecimal remainingMaturityPeriod) {
        BigDecimal haircut;
        if (remainingMaturityPeriod.compareTo(MATURITY_THRESHOLD_SHORT) <= 0) {
            haircut = HAIRCUT_SHORT;
        } else if (remainingMaturityPeriod.compareTo(MATURITY_THRESHOLD_MEDIUM) <= 0) {
            haircut = HAIRCUT_MEDIUM;
        } else {
            haircut = HAIRCUT_LONG;
        }
        return haircut.setScale(HAIRCUT_SCALE, RoundingMode.HALF_UP);
    }

    BigDecimal calculateHaircutValue(BigDecimal marketValue, BigDecimal haircutPercentage) {
        return marketValue.multiply(haircutPercentage);
    }

    BigDecimal calculateValueAfterHaircut(BigDecimal marketValue, BigDecimal haircutValue) {
        return marketValue.subtract(haircutValue);
    }

    BigDecimal calculateConcentrationRisk(BigDecimal marketValue, BigDecimal lastEquity) {
        if (lastEquity.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal threshold = lastEquity.multiply(CONCENTRATION_THRESHOLD_RATE);
        if (marketValue.compareTo(threshold) > 0) {
            return marketValue.subtract(threshold);
        }
        return BigDecimal.ZERO;
    }
}
