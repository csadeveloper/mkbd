package com.ciptadana.mkbd_master_menu.service.Repo;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Repo.RepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Repo.RepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.Repo.RepoJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoCounterPartyResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoJpaRepository repoJpaRepository;

    //    Muat daftar repo
    public List<RepoListResponse> getRepoList(String startDate, String endDate) {
        return repoJpaRepository.findRepoList(startDate, endDate);
    }

//    counter party repo & reverse repo

// calculate days for repo
// calculate days for penjaminan korporasi
    public BigDecimal calculateDays (String a, String b){
        return repoJpaRepository.findDays(a,b);
    }

    //    counter party repo & reverse repo
    public Map<String, Object> getRepoCounterParty(int page, int size, String name, String code) {
        int startRow = page * size;
        int endRow = startRow + size;
        List<RepoCounterPartyResponse> data = repoJpaRepository.findRepoCounterParty(name, code, startRow, endRow);
        long total = repoJpaRepository.countRepoCounterParty(name, code);

        Map<String, Object> result = new HashMap<>();
        result.put("content", data);
        result.put("page", page);
        result.put("size", size);
        result.put("totalElements", total);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        return result;
    }

    //    Insert repo baru
    @Transactional
    public void insertRepo(List<RepoInsertRequest> requests) {
        for (RepoInsertRequest request : requests) {
            repoJpaRepository.insertRepo(
                    request.getCounterParty(),
                    request.getNshare(),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getNominal(),
                    request.getReBuyingValue(),
                    request.getInitialDate(),
                    request.getDueDate(),
                    request.getRatio(),
                    request.getDays(),
                    request.getType(),
                    request.getNotes()
            );
        }
    }

    //    Update repo
    public void updateRepo(RepoUpdateRequest request) {
        repoJpaRepository.updateRepo(
                request.getRowid(),
                request.getCounterParty(),
                request.getNshare(),
                request.getQuantity(),
                request.getPrice(),
                request.getNominal(),
                request.getReBuyingValue(),
                request.getInitialDate(),
                request.getDueDate(),
                request.getRatio(),
                request.getDays(),
                request.getType(),
                request.getNotes()
        );
    }

    //    Delete repo
    public void deleteRepo(String rowid) {
        repoJpaRepository.deleteRepo(rowid);
    }

}
