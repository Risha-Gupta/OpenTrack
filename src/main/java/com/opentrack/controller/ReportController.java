package com.opentrack.controller;

import com.opentrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Map<String, Object>> monthlyReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        LocalDate now = LocalDate.now();
        int y = year != null ? year : now.getYear();
        int m = month != null ? month : now.getMonthValue();
        return ResponseEntity.ok(reportService.generateMonthlyReport(y, m));
    }

    @GetMapping("/weekly/{username}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Map<String, Object>> weeklyReport(@PathVariable String username) {
        return ResponseEntity.ok(reportService.generateWeeklyReport(username));
    }

    @GetMapping("/monthly/current")
    public ResponseEntity<Map<String, Object>> currentMonthReport() {
        LocalDate now = LocalDate.now();
        return ResponseEntity.ok(reportService.generateMonthlyReport(now.getYear(), now.getMonthValue()));
    }
}