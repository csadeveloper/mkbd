package com.ciptadana.mkbd_master_menu.service.ReverseRepo;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.ReverseRepo.ReverseRepoJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.ReverseRepo.ReverseRepoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReverseRepoService {

    private final ReverseRepoJpaRepository reverseRepoJpaRepository;

    //    Muat daftar reverse_repo
    public List<ReverseRepoListResponse> getReverseRepoList(String startDate, String endDate) {
        return reverseRepoJpaRepository.findReverseRepoList(startDate, endDate);
    }

    //    Insert reverse repo baru
    @Transactional
    public void insertReverseRepo(List<ReverseRepoInsertRequest> requests) {
        for (ReverseRepoInsertRequest request : requests) {
            reverseRepoJpaRepository.insertReverseRepo(
                    request.getCounterParty(),
                    request.getNshare(),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getNominal(),
                    request.getReSellingValue(),
                    request.getInitialDate(),
                    request.getDueDate(),
                    request.getRatio(),
                    request.getDays(),
                    request.getType(),
                    request.getNotes()
            );
        }
    }

    //    Update reverse repo
    public void updateReverseRepo(ReverseRepoUpdateRequest request) {
        reverseRepoJpaRepository.updateReverseRepo(
                request.getRowid(),
                request.getCounterParty(),
                request.getNshare(),
                request.getQuantity(),
                request.getPrice(),
                request.getNominal(),
                request.getReSellingValue(),
                request.getInitialDate(),
                request.getDueDate(),
                request.getRatio(),
                request.getDays(),
                request.getType(),
                request.getNotes()
        );
    }

    //    Delete reverse repo
    public void deleteReverseRepo(String rowid) {
        reverseRepoJpaRepository.deleteReverseRepo(rowid);
    }

}
