package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;
import com.choongang.frombirth_backend.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class WeeklyReportController {
    private final WeeklyReportService weeklyReportService;

    @Autowired
    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @GetMapping("/all/{childId}")
    public ResponseEntity<List<WeeklyReportDTO>> getAllReports(@PathVariable Integer childId) {
        return ResponseEntity.ok(weeklyReportService.getAllReports(childId));
    }

    @GetMapping("report/{reportId}")
    public ResponseEntity<WeeklyReportDTO> getReportById(@PathVariable Integer reportId) {
        return ResponseEntity.ok(weeklyReportService.getReportById(reportId));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> addReport(@RequestBody WeeklyReportDTO weeklyReportDTO) {
        weeklyReportService.addReport(weeklyReportDTO);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/delete/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer reportId) {
        weeklyReportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }


}
