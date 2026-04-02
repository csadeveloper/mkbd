package com.ciptadana.mkbd_master_menu.controller.Sbn;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnCalculationResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Sbn.SbnUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Sbn.SbnListResponse;
import com.ciptadana.mkbd_master_menu.service.Sbn.SbnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class SbnController {

    private final SbnService sbnService;

    @GetMapping("sbn/list")
    public ResponseEntity<List<SbnListResponse>> getSbnList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(sbnService.getSbnList(date));
    }

    @GetMapping("sbn/calculation")
    public ResponseEntity<List<SbnCalculationResponse>> getSbnCalculation(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(sbnService.getSbnCalculation(date));
    }

    @PostMapping("sbn/insert")
    public ResponseEntity<String> insertSbn(
            @RequestBody List<SbnInsertRequest> requests
    ) {
        sbnService.insertSbn(requests);
        return ResponseEntity.ok("Data SBN berhasil disimpan");
    }

    @PutMapping("sbn/update")
    public ResponseEntity<String> updateSbn(
            @RequestBody SbnUpdateRequest request
    ) {
        sbnService.updateSbn(request);
        return ResponseEntity.ok("Data SBN berhasil diupdate");
    }

    @DeleteMapping("sbn/delete")
    public ResponseEntity<String> deleteSbn(
            @RequestParam("rowid") String rowid
    ) {
        sbnService.deleteSbn(rowid);
        return ResponseEntity.ok("Data SBN berhasil dihapus");
    }

}
