package com.ciptadana.mkbd_master_menu.service.TransaksiValas;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.TransaksiValas.TransaksiValasInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.TransaksiValas.TransaksiValasUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.TransaksiValas.TransaksiValasListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.TransaksiValas.TransaksiValasJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransaksiValasService {

    private final TransaksiValasJpaRepository transaksiValasJpaRepository;

    public List<TransaksiValasListResponse> getTransaksiValasList(String date) {
        return transaksiValasJpaRepository.findTransaksiValasList(date);
    }

    @Transactional
    public void insertTransaksiValas(List<TransaksiValasInsertRequest> requests) {
        for (TransaksiValasInsertRequest request : requests) {
            transaksiValasJpaRepository.insertTransaksiValas(
                    request.getDealDate(),
                    request.getTypeTrx(),
                    request.getCurrencyTrx(),
                    request.getTrxVal(),
                    request.getUnrealizePl(),
                    request.getRl()
            );
        }
    }

    public void updateTransaksiValas(TransaksiValasUpdateRequest request) {
        transaksiValasJpaRepository.updateTransaksiValas(
                request.getRowid(),
                request.getDealDate(),
                request.getTypeTrx(),
                request.getCurrencyTrx(),
                request.getTrxVal(),
                request.getUnrealizePl(),
                request.getRl()
        );
    }

    public void deleteTransaksiValas(String rowid) {
        transaksiValasJpaRepository.deleteTransaksiValas(rowid);
    }

}