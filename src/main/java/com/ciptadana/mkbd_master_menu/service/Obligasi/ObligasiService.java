package com.ciptadana.mkbd_master_menu.service.Obligasi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Obligasi.ObligasiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Obligasi.ObligasiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Obligasi.ObligasiJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiBondNameResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiRatingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObligasiService {
    private final ObligasiJpaRepository obligasiJpaRepository;

//    Muat daftar obligasi
    public List<ObligasiListResponse> getObligasiList(String date) {
        return obligasiJpaRepository.findObligasiList(date);
    }

//    Muat dropdown rating
    public List<ObligasiRatingResponse> getObligasiRating() {
        return obligasiJpaRepository.findObligasiRating();
    }

//    Isi otomatis haircut saat rating dipilih
    public ObligasiRatingResponse getObligasiAutomatedRating(String rating) {
        return obligasiJpaRepository.findObligasiAutomatedRating(rating);
    }

// Pencarian nama obligasi (F1)
// Pencarian efek SBN  (F1)
// Pencarian efek Hedging (F1)
// Pencarian efek Repo (F1)
// Pencarian efek Reverse Repo (F1)
    public ObligasiBondNameResponse getObligasiBondName(String code) {
        return obligasiJpaRepository.findObligasiBondName(code);
    }

//    kalkulasi obligasi & sbn
    public BigDecimal getCalculationBusiness(String pdate) {
        return obligasiJpaRepository.findCalculationBusiness(pdate);
    }

//    Insert obligasi baru
    public void insertObligasi(ObligasiInsertRequest request) {
        obligasiJpaRepository.insertObligasi(
                request.getDealDate(),
                request.getNshareName(),
                request.getRating(),
                request.getQuantity(),
                request.getPrice(),
                request.getMarketValue(),
                request.getHaircutValue(),
                request.getHaircutAfter(),
                request.getConcernRisk(),
                request.getAcquisitionPrice(),
                request.getAffiliated(),
                request.getSukuk()
        );
    }

    public void deleteObligasi(String rowid) {
        obligasiJpaRepository.deleteObligasi(rowid);
    }

    public void updateObligasi(ObligasiUpdateRequest request) {
        obligasiJpaRepository.updateObligasi(
                request.getRowid(),
                request.getDealDate(),
                request.getNshareName(),
                request.getRating(),
                request.getQuantity(),
                request.getPrice(),
                request.getMarketValue(),
                request.getHaircutValue(),
                request.getHaircutAfter(),
                request.getConcernRisk(),
                request.getAcquisitionPrice(),
                request.getAffiliated(),
                request.getSukuk()
        );
    }
}
