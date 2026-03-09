package com.ciptadana.mkbd_master_menu.controller.Reksadana;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.ReverseRepo.ReverseRepoUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaIsinResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaListResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Reksadana.ReksadanaRiskResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.Repo.RepoListResponse;
import com.ciptadana.mkbd_master_menu.service.Reksadana.ReksadanaService;
import com.ciptadana.mkbd_master_menu.service.ReverseRepo.ReverseRepoService;
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

}
