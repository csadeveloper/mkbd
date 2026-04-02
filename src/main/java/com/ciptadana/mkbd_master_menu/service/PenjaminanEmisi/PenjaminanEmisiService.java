package com.ciptadana.mkbd_master_menu.service.PenjaminanEmisi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanEmisi.PenjaminanEmisiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanEmisi.PenjaminanEmisiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PenjaminanEmisi.PenjaminanEmisiJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanEmisi.PenjaminanEmisiListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PenjaminanEmisiService {

    private final PenjaminanEmisiJpaRepository penjaminanEmisiJpaRepository;

    //    Muat daftar penjaminan emisi
    public List<PenjaminanEmisiListResponse> getPenjaminanEmisiList(String date) {
        return penjaminanEmisiJpaRepository.findPenjaminanEmisiList(date);
    }

    //    Insert penjaminan emisi baru
    @Transactional
    public void insertPenjaminanEmisi(List<PenjaminanEmisiInsertRequest> requests) {
        for (PenjaminanEmisiInsertRequest request : requests) {
            penjaminanEmisiJpaRepository.insertPenjaminanEmisi(
                    request.getContractDate(),
                    request.getType(),
                    request.getCounterParty(),
                    request.getStatus(),
                    request.getPortionValue(),
                    request.getHaircutEfek(),
                    request.getAbsorbedVal(),
                    request.getCreditGuarantee(),
                    request.getRl()
            );
        }
    }

    //    Update penjaminan emisi
    public void updatePenjaminanEmisi(PenjaminanEmisiUpdateRequest request) {
        penjaminanEmisiJpaRepository.updatePenjaminanEmisi(
                request.getRowid(),
                request.getContractDate(),
                request.getType(),
                request.getCounterParty(),
                request.getStatus(),
                request.getPortionValue(),
                request.getHaircutEfek(),
                request.getAbsorbedVal(),
                request.getCreditGuarantee(),
                request.getRl()
        );
    }

    //    Delete penjaminan emisi
    public void deletePenjaminanEmisi(String rowid) {
        penjaminanEmisiJpaRepository.deletePenjaminanEmisi(rowid);
    }

}
