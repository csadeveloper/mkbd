package com.ciptadana.mkbd_master_menu.controller.Obligasi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Obligasi.ObligasiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Obligasi.ObligasiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiBondNameResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Obligasi.ObligasiRatingResponse;
import com.ciptadana.mkbd_master_menu.service.Obligasi.ObligasiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class ObligasiController {

    private final ObligasiService obligasiService;

    @GetMapping("obligasi/list")
    public ResponseEntity<List<ObligasiListResponse>> getObligasiList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(obligasiService.getObligasiList(date));
    }

    @GetMapping("obligasi/rating")
    public ResponseEntity<List<ObligasiRatingResponse>> getObligasiRating(
    ) {
        return ResponseEntity.ok(obligasiService.getObligasiRating());
    }

    @GetMapping("obligasi/automated/rating")
    public ResponseEntity<ObligasiRatingResponse> getObligasiAutomatedRating(
            @RequestParam("rating") String rating
            ) {
        return ResponseEntity.ok(obligasiService.getObligasiAutomatedRating(rating));
    }

    @GetMapping("obligasi/bond/name")
    public ResponseEntity<ObligasiBondNameResponse> getObligasiBondName(
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(obligasiService.getObligasiBondName(code));
    }

    @GetMapping("obligasi/calculation/business")
    public ResponseEntity<BigDecimal> getCalculationBusiness(
            @RequestParam("code") String pdate
    ) {
        return ResponseEntity.ok(obligasiService.getCalculationBusiness(pdate));
    }


    @PostMapping("obligasi/insert")
    public ResponseEntity<String> insertObligasi(
            @RequestBody ObligasiInsertRequest request
    ) {
        obligasiService.insertObligasi(request);
        return ResponseEntity.ok("Data obligasi berhasil disimpan");
    }

    @DeleteMapping("obligasi/delete")
    public ResponseEntity<String> deleteObligasi(
            @RequestParam("rowid") String rowid
    ) {
        obligasiService.deleteObligasi(rowid);
        return ResponseEntity.ok("Data obligasi berhasil dihapus");
    }

    @PutMapping("obligasi/update")
    public ResponseEntity<String> updateObligasi(
            @RequestBody ObligasiUpdateRequest request
    ) {
        obligasiService.updateObligasi(request);
        return ResponseEntity.ok("Data obligasi berhasil diupdate");
    }
}
