package com.ciptadana.mkbd_master_menu.service.Sbn;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Sbn.SbnJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SbnService {

    private final SbnJpaRepository sbnJpaRepository;

    //    Muat daftar sbn
    public List<SbnListResponse> getSbnList(String date) {
        return sbnJpaRepository.findSbnList(date);
    }

    //    Insert sbn baru
    public void insertSbn(SbnInsertRequest request) {
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
