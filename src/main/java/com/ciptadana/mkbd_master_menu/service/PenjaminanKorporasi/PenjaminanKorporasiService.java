package com.ciptadana.mkbd_master_menu.service.PenjaminanKorporasi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanKorporasi.PenjaminanKorporasiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanKorporasi.PenjaminanKorporasiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PenjaminanKorporasi.PenjaminanKorporasiJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanKorporasi.PenjaminanKorporasiListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PenjaminanKorporasiService {

    private final PenjaminanKorporasiJpaRepository penjaminanKorporasiJpaRepository;

    //    Muat daftar penjaminan korporasi
    public List<PenjaminanKorporasiListResponse> getPenjaminanKorporasiList(String date) {
        return penjaminanKorporasiJpaRepository.findPenjaminanKorporasiList(date);
    }

    //    Insert penjaminan korporasi baru
    @Transactional
    public void insertPenjaminanKorporasi(List<PenjaminanKorporasiInsertRequest> requests) {
        for (PenjaminanKorporasiInsertRequest request : requests) {
            penjaminanKorporasiJpaRepository.insertPenjaminanKorporasi(
                    request.getContractDate(),
                    request.getCounterParty(),
                    request.getAffiliated(),
                    request.getDetails(),
                    request.getDays(),
                    request.getDueDate(),
                    request.getAssuranceVal(),
                    request.getRl()
            );
        }
    }

    //    Update penjaminan korporasi
    public void updatePenjaminanKorporasi(PenjaminanKorporasiUpdateRequest request) {
        penjaminanKorporasiJpaRepository.updatePenjaminanKorporasi(
                request.getRowid(),
                request.getContractDate(),
                request.getCounterParty(),
                request.getAffiliated(),
                request.getDetails(),
                request.getDays(),
                request.getDueDate(),
                request.getAssuranceVal(),
                request.getRl()
        );
    }

    //    Delete penjaminan korporasi
    public void deletePenjaminanKorporasi(String rowid) {
        penjaminanKorporasiJpaRepository.deletePenjaminanKorporasi(rowid);
    }

}
