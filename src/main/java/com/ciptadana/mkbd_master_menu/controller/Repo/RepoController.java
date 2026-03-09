package com.ciptadana.mkbd_master_menu.controller.Repo;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Repo.RepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Repo.RepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoCounterPartyResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import com.ciptadana.mkbd_master_menu.service.Repo.RepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class RepoController {

    private final RepoService repoService;

    @GetMapping("repo/list")
    public ResponseEntity<List<RepoListResponse>> getRepoList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(repoService.getRepoList(date));
    }

    @GetMapping("repo/counterparty/name")
    public ResponseEntity<List<RepoCounterPartyResponse>> getRepoCounterPartyByName(
            @RequestParam("name") String name) {
        return ResponseEntity.ok(repoService.getRepoCounterPartyByName(name));
    }

    @GetMapping("repo/days")
    public ResponseEntity<BigDecimal> getCalculateDays(
            @RequestParam("due_date") String due_date,
            @RequestParam("initial_date") String initial_date
    ) {
        return ResponseEntity.ok(repoService.calculateDays(due_date, initial_date));
    }

    @GetMapping("repo/counterparty")
    public ResponseEntity<Map<String, Object>> getRepoCounterParty(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(repoService.getRepoCounterParty(page, size));
    }

    @PostMapping("repo/insert")
    public ResponseEntity<String> insertRepo(
            @RequestBody RepoInsertRequest request
    ) {
        repoService.insertRepo(request);
        return ResponseEntity.ok("Data Repo berhasil disimpan");
    }

    @PutMapping("repo/update")
    public ResponseEntity<String> updateRepo(
            @RequestBody RepoUpdateRequest request
    ) {
        repoService.updateRepo(request);
        return ResponseEntity.ok("Data Repo berhasil diupdate");
    }

    @DeleteMapping("repo/delete")
    public ResponseEntity<String> deleteRepo(
            @RequestParam("rowid") String rowid
    ) {
        repoService.deleteRepo(rowid);
        return ResponseEntity.ok("Data Repo berhasil dihapus");
    }

}
