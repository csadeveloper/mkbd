package com.ciptadana.mkbd_master_menu.controller.ReverseRepo;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.ReverseRepo.ReverseRepoListResponse;
import com.ciptadana.mkbd_master_menu.service.ReverseRepo.ReverseRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class ReverseRepoController {

    private final ReverseRepoService reverseRepoService;

    @GetMapping("reverseRepo/list")
    public ResponseEntity<List<ReverseRepoListResponse>> getReverseRepoList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(reverseRepoService.getReverseRepoList(date));
    }

    @PostMapping("reverseRepo/insert")
    public ResponseEntity<String> insertReverseRepo(
            @RequestBody List<ReverseRepoInsertRequest> requests
    ) {
        reverseRepoService.insertReverseRepo(requests);
        return ResponseEntity.ok("Data Reverse Repo berhasil disimpan");
    }

    @PutMapping("reverseRepo/update")
    public ResponseEntity<String> updateReverseRepo(
            @RequestBody ReverseRepoUpdateRequest request
    ) {
        reverseRepoService.updateReverseRepo(request);
        return ResponseEntity.ok("Data Reverse Repo berhasil diupdate");
    }

    @DeleteMapping("reverseRepo/delete")
    public ResponseEntity<String> deleteReverseRepo(
            @RequestParam("rowid") String rowid
    ) {
        reverseRepoService.deleteReverseRepo(rowid);
        return ResponseEntity.ok("Data Reverse Repo berhasil dihapus");
    }

}
