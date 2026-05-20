package com.ciptadana.mkbd_master_menu.controller.MkbdAllData;

import com.ciptadana.mkbd_master_menu.service.MkbdAllData.MkbdVdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mkbd/")
public class MkbdVdController {

    private final MkbdVdService service;

    // ===== DETAIL =====

    @GetMapping("vd51/detail")
    public ResponseEntity<Map<String, Object>> vd51Detail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd51", genDate));
    }

    @GetMapping("vd52/detail")
    public ResponseEntity<Map<String, Object>> vd52Detail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd52", genDate));
    }

    @GetMapping("vd56/detail")
    public ResponseEntity<Map<String, Object>> vd56Detail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd56", genDate));
    }

    @GetMapping("vd57b/detail")
    public ResponseEntity<Map<String, Object>> vd57bDetail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd57b", genDate));
    }

    @GetMapping("vd57c/detail")
    public ResponseEntity<Map<String, Object>> vd57cDetail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd57c", genDate));
    }

    @GetMapping("vd57d/detail")
    public ResponseEntity<Map<String, Object>> vd57dDetail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd57d", genDate));
    }

    @GetMapping("vd57e/detail")
    public ResponseEntity<Map<String, Object>> vd57eDetail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "vd57e", genDate));
    }

    @GetMapping("coa/detail")
    public ResponseEntity<Map<String, Object>> coaDetail(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("detail", "coa", genDate));
    }

    // ===== SUMMARY =====

    @GetMapping("vd51/summary")
    public ResponseEntity<Map<String, Object>> vd51Summary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd51", genDate));
    }

    @GetMapping("vd52/summary")
    public ResponseEntity<Map<String, Object>> vd52Summary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd52", genDate));
    }

    @GetMapping("vd56/summary")
    public ResponseEntity<Map<String, Object>> vd56Summary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd56", genDate));
    }

    @GetMapping("vd57b/summary")
    public ResponseEntity<Map<String, Object>> vd57bSummary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd57b", genDate));
    }

    @GetMapping("vd57c/summary")
    public ResponseEntity<Map<String, Object>> vd57cSummary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd57c", genDate));
    }

    @GetMapping("vd57d/summary")
    public ResponseEntity<Map<String, Object>> vd57dSummary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd57d", genDate));
    }

    @GetMapping("vd57e/summary")
    public ResponseEntity<Map<String, Object>> vd57eSummary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "vd57e", genDate));
    }

    @GetMapping("coa/summary")
    public ResponseEntity<Map<String, Object>> coaSummary(@RequestParam("genDate") String genDate) {
        return ResponseEntity.ok(service.getByType("summary", "coa", genDate));
    }
}
