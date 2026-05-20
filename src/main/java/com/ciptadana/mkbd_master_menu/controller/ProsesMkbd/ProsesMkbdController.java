package com.ciptadana.mkbd_master_menu.controller.ProsesMkbd;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ProsesMkbd.ProsesMkbdResponse;
import com.ciptadana.mkbd_master_menu.service.ProsesMkbd.ProsesMkbdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/")
public class ProsesMkbdController {

    private final ProsesMkbdService prosesMkbdService;

    @PostMapping("proses/run")
    public ResponseEntity<ProsesMkbdResponse> runProsesMkbd() {
        return ResponseEntity.ok(prosesMkbdService.runProsesMkbd());
    }
}
