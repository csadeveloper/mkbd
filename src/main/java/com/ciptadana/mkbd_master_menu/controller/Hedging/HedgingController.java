package com.ciptadana.mkbd_master_menu.controller.Hedging;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Hedging.HedgingInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Hedging.HedgingUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingEfekResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Hedging.HedgingListResponse;
import com.ciptadana.mkbd_master_menu.service.Hedging.HedgingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class HedgingController {

    private final HedgingService hedgingService;

    @GetMapping("hedging/list")
    public ResponseEntity<List<HedgingListResponse>> getHedgingList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(hedgingService.getHedgingList(date));
    }

    @GetMapping("hedging/efek")
    public ResponseEntity<List<HedgingEfekResponse>> getHedgingEfek(
            @RequestParam("code") String code) {
        return ResponseEntity.ok(hedgingService.getHedgingEfek(code));
    }

    @PostMapping("hedging/insert")
    public ResponseEntity<String> insertHedging(
            @RequestBody List<HedgingInsertRequest> requests
    ) {
        hedgingService.insertHedging(requests);
        return ResponseEntity.ok("Data Hedging berhasil disimpan");
    }

    @PutMapping("hedging/update")
    public ResponseEntity<String> updateHedging(
            @RequestBody HedgingUpdateRequest request
    ) {
        hedgingService.updateHedging(request);
        return ResponseEntity.ok("Data Hedging berhasil diupdate");
    }

    @DeleteMapping("hedging/delete")
    public ResponseEntity<String> deleteHedging(
            @RequestParam("rowid") String rowid
    ) {
        hedgingService.deleteHedging(rowid);
        return ResponseEntity.ok("Data Hedging berhasil dihapus");
    }

}
