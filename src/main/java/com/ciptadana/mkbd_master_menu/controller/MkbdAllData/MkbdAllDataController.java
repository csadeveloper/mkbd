package com.ciptadana.mkbd_master_menu.controller.MkbdAllData;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.MkbdAllDataResponse;
import com.ciptadana.mkbd_master_menu.service.MkbdAllData.MkbdAllDataFullService;
import com.ciptadana.mkbd_master_menu.service.MkbdAllData.MkbdAllDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/")
public class MkbdAllDataController {

    private final MkbdAllDataService mkbdAllDataService;
    private final MkbdAllDataFullService mkbdAllDataFullService;

    @GetMapping("all-data")
    public ResponseEntity<MkbdAllDataResponse> getAllData(
            @RequestParam("genDate") String genDate
    ) {
        return ResponseEntity.ok(mkbdAllDataService.getAllData(genDate));
    }

    @GetMapping("all-data-full")
    public ResponseEntity<Map<String, Object>> getAllDataFull(
            @RequestParam("genDate") String genDate
    ) {
        return ResponseEntity.ok(mkbdAllDataFullService.getAllData(genDate));
    }
}
