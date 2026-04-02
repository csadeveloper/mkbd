package com.ciptadana.mkbd_master_menu.controller.PenjaminanKorporasi;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanKorporasi.PenjaminanKorporasiInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.PenjaminanKorporasi.PenjaminanKorporasiUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PenjaminanKorporasi.PenjaminanKorporasiListResponse;
import com.ciptadana.mkbd_master_menu.service.PenjaminanKorporasi.PenjaminanKorporasiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class PenjaminanKorporasiController {

    private final PenjaminanKorporasiService penjaminanKorporasiService;

    @GetMapping("penjaminan/korporasi/list")
    public ResponseEntity<List<PenjaminanKorporasiListResponse>> getPenjaminanKorporasiList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(penjaminanKorporasiService.getPenjaminanKorporasiList(date));
    }

    @PostMapping("penjaminan/korporasi/insert")
    public ResponseEntity<String> insertPenjaminanKorporasi(
            @RequestBody List<PenjaminanKorporasiInsertRequest> requests
    ) {
        penjaminanKorporasiService.insertPenjaminanKorporasi(requests);
        return ResponseEntity.ok("Data Penjaminan Korporasi berhasil disimpan");
    }

    @PutMapping("penjaminan/korporasi/update")
    public ResponseEntity<String> updatePenjaminanKorporasi(
            @RequestBody PenjaminanKorporasiUpdateRequest request
    ) {
        penjaminanKorporasiService.updatePenjaminanKorporasi(request);
        return ResponseEntity.ok("Data Penjaminan Korporasi berhasil diupdate");
    }

    @DeleteMapping("penjaminan/korporasi/delete")
    public ResponseEntity<String> deletePenjaminanKorporasi(
            @RequestParam("rowid") String rowid
    ) {
        penjaminanKorporasiService.deletePenjaminanKorporasi(rowid);
        return ResponseEntity.ok("Data Penjaminan Korporasi berhasil dihapus");
    }

}
