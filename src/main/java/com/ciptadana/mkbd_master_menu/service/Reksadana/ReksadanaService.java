package com.ciptadana.mkbd_master_menu.service.Reksadana;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Reksadana.ReksadanaJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.ReverseRepo.ReverseRepoJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaIsinResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaRiskResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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



}
