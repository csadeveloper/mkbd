package com.ciptadana.mkbd_master_menu.controller.PengaturanParameter;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PengaturanParameter.ApplicationParameterResponse;
import com.ciptadana.mkbd_master_menu.service.PengaturanParameter.PengaturanParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/master/")
public class PengaturanParameterController {

    private final PengaturanParameterService pengaturanParameterService;

    @GetMapping("pengaturan/parameter/aplikasi")
    public ResponseEntity<List<ApplicationParameterResponse>> getParameterList(
    ) {
        return ResponseEntity.ok(pengaturanParameterService.getParameterList());
    }

    @PutMapping("pengaturan/parameter/update")
    public ResponseEntity<String> updateParameter(
            @RequestParam("code") String code,
            @RequestParam("value") String value
    ) {
        pengaturanParameterService.updateParameter(code, value);
        return ResponseEntity.ok("Parameter " + code + " berhasil diupdate");
    }

}
