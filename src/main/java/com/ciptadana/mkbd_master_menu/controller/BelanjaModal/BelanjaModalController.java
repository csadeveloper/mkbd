package com.ciptadana.mkbd_master_menu.controller.BelanjaModal;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.BelanjaModal.BelanjaModalInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.BelanjaModal.BelanjaModalUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.BelanjaModal.BelanjaModalListResponse;
import com.ciptadana.mkbd_master_menu.service.BelanjaModal.BelanjaModalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class BelanjaModalController {

    private final BelanjaModalService belanjaModalService;

    @GetMapping("belanja/modal/list")
    public ResponseEntity<List<BelanjaModalListResponse>> getBelanjaModalList(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(belanjaModalService.getBelanjaModalList(date));
    }

    @PostMapping("belanja/modal/insert")
    public ResponseEntity<String> insertBelanjaModal(
            @RequestBody List<BelanjaModalInsertRequest> requests
    ) {
        belanjaModalService.insertBelanjaModal(requests);
        return ResponseEntity.ok("Data Belanja Modal berhasil disimpan");
    }

    @PutMapping("belanja/modal/update")
    public ResponseEntity<String> updateBelanjaModal(
            @RequestBody BelanjaModalUpdateRequest request
    ) {
        belanjaModalService.updateBelanjaModal(request);
        return ResponseEntity.ok("Data Belanja Modal berhasil diupdate");
    }

    @DeleteMapping("belanja/modal/delete")
    public ResponseEntity<String> deleteBelanjaModal(
            @RequestParam("rowid") String rowid
    ) {
        belanjaModalService.deleteBelanjaModal(rowid);
        return ResponseEntity.ok("Data Belanja Modal berhasil dihapus");
    }

}
