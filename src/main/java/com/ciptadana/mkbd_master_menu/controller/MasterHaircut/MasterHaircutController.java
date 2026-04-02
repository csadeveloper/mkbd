package com.ciptadana.mkbd_master_menu.controller.MasterHaircut;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutHistInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterHaircut.HaircutUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterHaircut.HaircutResponse;
import com.ciptadana.mkbd_master_menu.service.MasterHaircut.MasterHaircutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/")
public class MasterHaircutController {

    private final MasterHaircutService masterHaircutService;

    @GetMapping("master/haircut/list")
    public ResponseEntity<List<HaircutResponse>> getHaircutList(
    ) {
        return ResponseEntity.ok(masterHaircutService.getHaircutList());
    }

    @GetMapping("master/haircut/last/date")
    public ResponseEntity<String> getLastDatesList(
    ) {
        return ResponseEntity.ok(masterHaircutService.getLastUpdate());
    }

    @PostMapping("master/haircut/insert")
    public ResponseEntity<String> insertHaircutHist(
            @RequestBody List<HaircutHistInsertRequest> requests
    ) {
        masterHaircutService.insertHaircutHist(requests);
        return ResponseEntity.ok("Data Haircut History berhasil disimpan");
    }

    @DeleteMapping("master/haircut/delete/code")
    public ResponseEntity<String> deleteHaircut(
            @RequestParam("code") String code
    ) {
        masterHaircutService.deleteHaircut(code);
        return ResponseEntity.ok("Data Haircut berhasil dihapus");
    }

    @PostMapping("master/haircut/insert/data")
    public ResponseEntity<String> insertHaircut(
            @RequestBody List<HaircutInsertRequest> requests
    ) {
        masterHaircutService.insertHaircut(requests);
        return ResponseEntity.ok("Data Haircut berhasil disimpan");
    }

    @DeleteMapping("master/haircut/delete")
    public ResponseEntity<String> deleteHaircutHist(
            @RequestParam("date") String date
    ) {
        masterHaircutService.deleteHaircutHist(date);
        return ResponseEntity.ok("Data Haircut History berhasil dihapus");
    }

    @PutMapping("master/haircut/update")
    public ResponseEntity<String> updateHaircut(
            @RequestBody HaircutUpdateRequest request
    ) {
        masterHaircutService.updateHaircut(request);
        return ResponseEntity.ok("Data Haircut berhasil diupdate");
    }

    @PutMapping("master/haircut/update/last")
    public ResponseEntity<String> updateLastUploadHaircut(
            @RequestParam("date") String date
    ) {
        masterHaircutService.updateLastUploadHaircut(date);
        return ResponseEntity.ok("Last Upload Haircut berhasil diupdate");
    }

}
