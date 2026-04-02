package com.ciptadana.mkbd_master_menu.service.Hedging;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Hedging.HedgingInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Hedging.HedgingUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Hedging.HedghingJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingEfekResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HedgingService {

    private final HedghingJpaRepository hedghingJpaRepository;

    //    Muat daftar hedging
    public List<HedgingListResponse> getHedgingList(String date) {
        return hedghingJpaRepository.findHedgingList(date);
    }

    public List<HedgingEfekResponse> getHedgingEfek(String code) {
        return hedghingJpaRepository.findHedgingEfek(code);
    }

    //    Insert hedging baru
    @Transactional
    public void insertHedging(List<HedgingInsertRequest> requests) {
        for (HedgingInsertRequest request : requests) {
            hedghingJpaRepository.insertHedging(
                    request.getDueDate(),
                    request.getNshare(),
                    request.getNominal(),
                    request.getNominalHedging(),
                    request.getHedgingVal(),
                    request.getHcHedgingVal(),
                    request.getHcVal()
            );
        }
    }

    //    Delete hedging
    public void deleteHedging(String rowid) {
        hedghingJpaRepository.deleteHedging(rowid);
    }

    //    Update hedging
    public void updateHedging(HedgingUpdateRequest request) {
        hedghingJpaRepository.updateHedging(
                request.getRowid(),
                request.getDueDate(),
                request.getNshare(),
                request.getNominal(),
                request.getNominalHedging(),
                request.getHedgingVal(),
                request.getHcHedgingVal(),
                request.getHcVal()
        );
    }

}
