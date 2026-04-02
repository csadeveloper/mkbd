package com.ciptadana.mkbd_master_menu.service.BelanjaModal;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.BelanjaModal.BelanjaModalInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.BelanjaModal.BelanjaModalUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.BelanjaModal.BelanjaModalJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.BelanjaModal.BelanjaModalListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BelanjaModalService {

    private final BelanjaModalJpaRepository belanjaModalJpaRepository;

    public List<BelanjaModalListResponse> getBelanjaModalList(String date) {
        return belanjaModalJpaRepository.findBelanjaModalList(date);
    }

    @Transactional
    public void insertBelanjaModal(List<BelanjaModalInsertRequest> requests) {
        for (BelanjaModalInsertRequest request : requests) {
            belanjaModalJpaRepository.insertBelanjaModal(
                    request.getCommitmentDate(),
                    request.getDetails(),
                    request.getDueDate(),
                    request.getRealizeVal(),
                    request.getUnrealizeVal(),
                    request.getRl()
            );
        }
    }

    public void updateBelanjaModal(BelanjaModalUpdateRequest request) {
        belanjaModalJpaRepository.updateBelanjaModal(
                request.getRowid(),
                request.getCommitmentDate(),
                request.getDetails(),
                request.getDueDate(),
                request.getRealizeVal(),
                request.getUnrealizeVal(),
                request.getRl()
        );
    }

    public void deleteBelanjaModal(String rowid) {
        belanjaModalJpaRepository.deleteBelanjaModal(rowid);
    }

}
