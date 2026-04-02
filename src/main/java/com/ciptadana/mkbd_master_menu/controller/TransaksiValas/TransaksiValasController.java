package com.ciptadana.mkbd_master_menu.controller.TransaksiValas;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.TransaksiValas.TransaksiValasInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.TransaksiValas.TransaksiValasUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.TransaksiValas.TransaksiValasListResponse;
import com.ciptadana.mkbd_master_menu.service.TransaksiValas.TransaksiValasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class TransaksiValasController {

    private final TransaksiValasService transaksiValasService;

    @GetMapping("transaksi/valas/list")
    public ResponseEntity<List<TransaksiValasListResponse>> getTransaksiValasList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(transaksiValasService.getTransaksiValasList(date));
    }

    @PostMapping("transaksi/valas/insert")
    public ResponseEntity<String> insertTransaksiValas(
            @RequestBody List<TransaksiValasInsertRequest> requests
    ) {
        transaksiValasService.insertTransaksiValas(requests);
        return ResponseEntity.ok("Data Transaksi Valas berhasil disimpan");
    }

    @PutMapping("transaksi/valas/update")
    public ResponseEntity<String> updateTransaksiValas(
            @RequestBody TransaksiValasUpdateRequest request
    ) {
        transaksiValasService.updateTransaksiValas(request);
        return ResponseEntity.ok("Data Transaksi Valas berhasil diupdate");
    }

    @DeleteMapping("transaksi/valas/delete")
    public ResponseEntity<String> deleteTransaksiValas(
            @RequestParam("rowid") String rowid
    ) {
        transaksiValasService.deleteTransaksiValas(rowid);
        return ResponseEntity.ok("Data Transaksi Valas berhasil dihapus");
    }

}