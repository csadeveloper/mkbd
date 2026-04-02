package com.ciptadana.mkbd_master_menu.service.Reksadana;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Reksadana.ReksadanaInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Reksadana.ReksadanaUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Reksadana.ReksadanaJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaIsinResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaRiskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReksadanaService {

    private final ReksadanaJpaRepository reksadanaJpaRepository;

    //    Muat daftar reksadana
    public List<ReksadanaListResponse> getReksadanaList(String date) {
        return reksadanaJpaRepository.findReksadanaList(date);
    }

//    Muat dropdown Type
    public List<ReksadanaRiskResponse> getReksadanaRisk() {
        return reksadanaJpaRepository.findReksadanaRisk();
    }

    public List<ReksadanaIsinResponse> getReksadanaIsin(String type, String input) {
        if (type.equals("1")) {
            return reksadanaJpaRepository.findReksadanaIsin();
        } else if (type.equals("2")) {
            return reksadanaJpaRepository.findReksadanaIsin(input);
        } else if (type.equals("3")){
            return reksadanaJpaRepository.findReksadanaIsinLoad(input);
        }
        return Collections.emptyList(); // or throw, depending on your contract
    }

    //    Insert reksadana baru
    @Transactional
    public void insertReksadana(List<ReksadanaInsertRequest> requests) {
        for (ReksadanaInsertRequest request : requests) {
            reksadanaJpaRepository.insertReksadana(
                    request.getDealDate(),
                    request.getType(),
                    request.getNshareName(),
                    request.getAmountUp(),
                    request.getLastNab(),
                    request.getTotalNabMi(),
                    request.getAffiliated()
            );
        }
    }

    //    Update reksadana
    public void updateReksadana(ReksadanaUpdateRequest request) {
        reksadanaJpaRepository.updateReksadana(
                request.getRowid(),
                request.getDealDate(),
                request.getType(),
                request.getNshareName(),
                request.getAmountUp(),
                request.getLastNab(),
                request.getTotalNabMi(),
                request.getAffiliated()
        );
    }

    //    Delete reksadana
    public void deleteReksadana(String rowid) {
        reksadanaJpaRepository.deleteReksadana(rowid);
    }

//    we use this date to all services
    public String getCurrentDate() {
        return reksadanaJpaRepository.getCurrentDate();
    }


}
