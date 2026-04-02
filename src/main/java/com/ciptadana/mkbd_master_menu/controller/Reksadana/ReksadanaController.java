package com.ciptadana.mkbd_master_menu.controller.Reksadana;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Reksadana.ReksadanaInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.Reksadana.ReksadanaUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaIsinResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaRiskResponse;
import com.ciptadana.mkbd_master_menu.service.Reksadana.ReksadanaService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class ReksadanaController {

    private final ReksadanaService reksadanaService;

    @GetMapping("reksadana/list")
    public ResponseEntity<List<ReksadanaListResponse>> getReksadanaList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(reksadanaService.getReksadanaList(date));
    }

    @GetMapping("reksadana/risk")
    public ResponseEntity<List<ReksadanaRiskResponse>> getReksadanaRisk(
    ) {
        return ResponseEntity.ok(reksadanaService.getReksadanaRisk());
    }

    @GetMapping("reksadana/isin")
    public ResponseEntity<List<ReksadanaIsinResponse>> getReksadanaIsin(
            @Parameter(
                    description = "1 = Semua isin reksadana, 2 = filter prefix, 3 = load reksadana"
            )
            @RequestParam(value = "type",defaultValue = "1") String type,
            @RequestParam(value = "input", required = false) String input
    ) {
        return ResponseEntity.ok(reksadanaService.getReksadanaIsin(type, input));
    }

    @PostMapping("reksadana/insert")
    public ResponseEntity<String> insertReksadana(
            @RequestBody List<ReksadanaInsertRequest> requests
    ) {
        reksadanaService.insertReksadana(requests);
        return ResponseEntity.ok("Data Reksadana berhasil disimpan");
    }

    @PutMapping("reksadana/update")
    public ResponseEntity<String> updateReksadana(
            @RequestBody ReksadanaUpdateRequest request
    ) {
        reksadanaService.updateReksadana(request);
        return ResponseEntity.ok("Data Reksadana berhasil diupdate");
    }

    @DeleteMapping("reksadana/delete")
    public ResponseEntity<String> deleteReksadana(
            @RequestParam("rowid") String rowid
    ) {
        reksadanaService.deleteReksadana(rowid);
        return ResponseEntity.ok("Data Reksadana berhasil dihapus");
    }

    @GetMapping("validate/date")
    public ResponseEntity<String> getCurrentDate() {
        return ResponseEntity.ok(reksadanaService.getCurrentDate());
    }

}
