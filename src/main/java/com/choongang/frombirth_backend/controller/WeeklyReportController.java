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

    @GetMapping("/cnt/{childId}")
    public ResponseEntity<Integer> getAllRecordCount(@PathVariable Integer childId) {
        return ResponseEntity.ok(weeklyReportService.getAllReportCount(childId));
    }

    @GetMapping("/report/{reportId}")
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

    // PUT 메서드 추가: 보고서의 'read' 값을 업데이트하는 메서드
    @PutMapping("/update/{reportId}")
    public ResponseEntity<Void> updateReportReadStatus(@PathVariable Integer reportId, @RequestBody WeeklyReportDTO weeklyReportDTO) {
        // 'read' 상태를 업데이트하는 서비스 메서드를 호출
        boolean updated = weeklyReportService.updateReportReadStatus(reportId, weeklyReportDTO.isRead());

        if (updated) {
            return ResponseEntity.ok().build();  // 성공적으로 업데이트한 경우
        } else {
            return ResponseEntity.status(400).build();  // 업데이트 실패한 경우
        }
    }
}