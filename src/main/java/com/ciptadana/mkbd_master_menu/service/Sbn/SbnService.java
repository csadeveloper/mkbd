package com.ciptadana.mkbd_master_menu.service.Sbn;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnCalculationResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Sbn.SbnJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SbnService {

    private final SbnJpaRepository sbnJpaRepository;
    private final SbnCalculationService sbnCalculationService;

    //    Muat daftar sbn
    public List<SbnListResponse> getSbnList(String date) {
        return sbnJpaRepository.findSbnList(date);
    }

    public List<SbnCalculationResponse> getSbnCalculation(String date) {
        LocalDate asOfDate = LocalDate.parse(date);
        List<SbnListResponse> sbnList = sbnJpaRepository.findSbnList(date);
        BigDecimal lastEquity = sbnJpaRepository.findLastEquity(date);
        if (lastEquity == null) {
            lastEquity = BigDecimal.ZERO;
        }
        return sbnCalculationService.calculateAll(sbnList, asOfDate, lastEquity);
    }

    //    Insert sbn baru
    @Transactional
    public void insertSbn(List<SbnInsertRequest> requests) {
        for (SbnInsertRequest request : requests) {
            sbnJpaRepository.insertSbn(
                    request.getNshare(),
                    request.getDueDate(),
                    request.getPrice(),
                    request.getNominal(),
                    request.getMarketValue(),
                    request.getAcquisitionPrice(),
                    request.getAffiliated()
            );
        }
    }

    //    Update sbn
    public void updateSbn(SbnUpdateRequest request) {
        sbnJpaRepository.updateSbn(
                request.getRowid(),
                request.getNshare(),
                request.getDueDate(),
                request.getPrice(),
                request.getNominal(),
                request.getMarketValue(),
                request.getAcquisitionPrice(),
                request.getAffiliated()
        );
    }

    //    Delete sbn
    public void deleteSbn(String rowid) {
        sbnJpaRepository.deleteSbn(rowid);
    }

}
