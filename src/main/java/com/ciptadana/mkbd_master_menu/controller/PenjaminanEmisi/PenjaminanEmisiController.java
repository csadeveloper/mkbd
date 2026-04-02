package com.ciptadana.mkbd_master_menu.controller.PenjaminanEmisi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanEmisi.PenjaminanEmisiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanEmisi.PenjaminanEmisiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanEmisi.PenjaminanEmisiListResponse;
import com.ciptadana.mkbd_master_menu.service.PenjaminanEmisi.PenjaminanEmisiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class PenjaminanEmisiController {

    private final PenjaminanEmisiService penjaminanEmisiService;

    @GetMapping("penjaminan/emisi/list")
    public ResponseEntity<List<PenjaminanEmisiListResponse>> getPenjaminanEmisiList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(penjaminanEmisiService.getPenjaminanEmisiList(date));
    }

    @PostMapping("penjaminan/emisi/insert")
    public ResponseEntity<String> insertPenjaminanEmisi(
            @RequestBody List<PenjaminanEmisiInsertRequest> requests
    ) {
        penjaminanEmisiService.insertPenjaminanEmisi(requests);
        return ResponseEntity.ok("Data Penjaminan Emisi berhasil disimpan");
    }

    @PutMapping("penjaminan/emisi/update")
    public ResponseEntity<String> updatePenjaminanEmisi(
            @RequestBody PenjaminanEmisiUpdateRequest request
    ) {
        penjaminanEmisiService.updatePenjaminanEmisi(request);
        return ResponseEntity.ok("Data Penjaminan Emisi berhasil diupdate");
    }

    @DeleteMapping("penjaminan/emisi/delete")
    public ResponseEntity<String> deletePenjaminanEmisi(
            @RequestParam("rowid") String rowid
    ) {
        penjaminanEmisiService.deletePenjaminanEmisi(rowid);
        return ResponseEntity.ok("Data Penjaminan Emisi berhasil dihapus");
    }

}
