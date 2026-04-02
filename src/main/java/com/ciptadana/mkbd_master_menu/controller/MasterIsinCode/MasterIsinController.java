package com.ciptadana.mkbd_master_menu.controller.MasterIsinCode;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterIsinCode.IsinReksadanaResponse;
import com.ciptadana.mkbd_master_menu.service.MasterIsinCode.MasterIsinService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/")
public class MasterIsinController {

    private final MasterIsinService masterIsinService;

    @GetMapping("master/isin/reksadana")
    public ResponseEntity<List<IsinReksadanaResponse>> getReksadanaList(
            @Parameter(
                    description = "1 = without condition, 2 = with condition"
            )
            @RequestParam("type") String type,
            @RequestParam(value = "prefix",required = false) String prefix
    ) {
        return ResponseEntity.ok(masterIsinService.getIsinReksadana(type, prefix));
    }

    @DeleteMapping("master/isin/delete")
    public ResponseEntity<String> deleteIsinCode(
            @RequestParam("type") String type,
            @RequestParam("isincode") String isincode
    ) {
        masterIsinService.deleteIsinCode(type, isincode);
        return ResponseEntity.ok("Data Isin Code berhasil dihapus");
    }

    @PutMapping("master/isin/update")
    public ResponseEntity<String> updateIsinCode(
            @RequestBody IsinCodeUpdateRequest request
    ) {
        masterIsinService.updateIsinCode(request);
        return ResponseEntity.ok("Data Isin Code berhasil diupdate");
    }

    @PostMapping("master/isin/insert")
    public ResponseEntity<String> insertIsinCode(
            @RequestBody List<IsinCodeInsertRequest> requests
    ) {
        masterIsinService.insertIsinCode(requests);
        return ResponseEntity.ok("Data Isin Code berhasil disimpan");
    }

    @DeleteMapping("master/isin/xdm/delete")
    public ResponseEntity<String> deleteXdmMasterIsin(
            @RequestParam("seccode") String seccode,
            @RequestParam("isinCode") String isinCode
    ) {
        masterIsinService.deleteXdmMasterIsin(seccode, isinCode);
        return ResponseEntity.ok("Data XDM Master Isin berhasil dihapus");
    }

    @PutMapping("master/isin/xdm/update")
    public ResponseEntity<String> updateXdmMasterIsin(
            @RequestBody XdmMasterIsinUpdateRequest request
    ) {
        masterIsinService.updateXdmMasterIsin(request);
        return ResponseEntity.ok("Data XDM Master Isin berhasil diupdate");
    }

    @PostMapping("master/isin/xdm/insert")
    public ResponseEntity<String> insertXdmMasterIsin(
            @RequestBody List<XdmMasterIsinInsertRequest> requests
    ) {
        masterIsinService.insertXdmMasterIsin(requests);
        return ResponseEntity.ok("Data XDM Master Isin berhasil disimpan");
    }

}