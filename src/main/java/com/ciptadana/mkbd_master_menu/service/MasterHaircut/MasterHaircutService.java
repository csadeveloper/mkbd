package com.ciptadana.mkbd_master_menu.service.MasterHaircut;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutHistInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterHaircut.MasterHaircutJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterHaircutService {

    private final MasterHaircutJpaRepository masterHaircutJpaRepository;

    public List<HaircutResponse> getHaircutList() {
        return masterHaircutJpaRepository.findHaircutList();
    }

    public String getLastUpdate() {
        return masterHaircutJpaRepository.findLastUpdateDates();
    }

    public void deleteHaircutHist(String date) {
        masterHaircutJpaRepository.deleteHaircutHist(date);
    }

    @Transactional
    public void insertHaircutHist(List<HaircutHistInsertRequest> requests) {
        for (HaircutHistInsertRequest request : requests) {
            masterHaircutJpaRepository.insertHaircutHist(
                    request.getRecDate(),
                    request.getCode(),
                    request.getName(),
                    request.getKomite(),
                    request.getMkbd(),
                    request.getNotes()
            );
        }
    }

    public void deleteHaircut(String code) {
        masterHaircutJpaRepository.deleteHaircut(code);
    }

    @Transactional
    public void insertHaircut(List<HaircutInsertRequest> requests) {
        for (HaircutInsertRequest request : requests) {
            masterHaircutJpaRepository.insertHaircut(
                    request.getDate(),
                    request.getCode(),
                    request.getName(),
                    request.getMkbd(),
                    request.getNotes()
            );
        }
    }

    public void updateHaircut(HaircutUpdateRequest request) {
        masterHaircutJpaRepository.updateHaircut(
                request.getCode(),
                request.getName(),
                request.getKomite(),
                request.getMkbd(),
                request.getNotes()
        );
    }

    public void updateLastUploadHaircut(String date) {
        masterHaircutJpaRepository.updateLastUploadHaircut(date);
    }

}
