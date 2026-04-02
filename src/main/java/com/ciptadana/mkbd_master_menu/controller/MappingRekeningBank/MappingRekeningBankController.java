package com.ciptadana.mkbd_master_menu.controller.MappingRekeningBank;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MappingRekeningBank.MapBankInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.AutomatedAccountResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.F1Response;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankAutomatedResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankListResponse;
import com.ciptadana.mkbd_master_menu.service.MappingRekeningBank.MappingRekeningBankService;
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
public class MappingRekeningBankController {

    private final MappingRekeningBankService mappingRekeningBankService;

    @GetMapping("mapping/rekening/bank/list")
    public ResponseEntity<List<MappingRekeningBankListResponse>> getMappingRekeningBankList(
            @Parameter(
                    description = "1 = without condition, 2 = with condition"
            )
            @RequestParam("type") String type,
            @RequestParam(value = "nclient",required = false) String nclient
    ) {
        return ResponseEntity.ok(mappingRekeningBankService.getMappingRekeningBankList(type, nclient));
    }

    @GetMapping("mapping/rekening/bank/automated")
    public ResponseEntity<MappingRekeningBankAutomatedResponse> getMappingRekeningBankAutomatedResponse(
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(mappingRekeningBankService.getMappingRekeningBankAutomatedResponse(code));
    }

    @GetMapping("/automated")
    public ResponseEntity<AutomatedAccountResponse> getAutomatedAccountResponse(
            @Parameter(
                    description = "Jika kode diawali 'M'"
            )
            @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(mappingRekeningBankService.getAutomatedAccountResponse(code));
    }

    @GetMapping("mapping/rekening/bank/f1/search")
    public ResponseEntity<List<F1Response>> getF1Response(
            @RequestParam(value = "prefix", required = false) String prefix
    ) {
        return ResponseEntity.ok(mappingRekeningBankService.getF1Response(prefix));
    }

    @DeleteMapping("mapping/rekening/bank/delete")
    public ResponseEntity<String> deleteMapBank(
            @RequestParam("nclient") String nclient
    ) {
        mappingRekeningBankService.deleteMapBank(nclient);
        return ResponseEntity.ok("Data Map Bank berhasil dihapus");
    }

    @PostMapping("mapping/rekening/bank/insert")
    public ResponseEntity<String> insertMapBank(
            @RequestBody List<MapBankInsertRequest> requests
    ) {
        mappingRekeningBankService.insertMapBank(requests);
        return ResponseEntity.ok("Data Map Bank berhasil disimpan");
    }

}