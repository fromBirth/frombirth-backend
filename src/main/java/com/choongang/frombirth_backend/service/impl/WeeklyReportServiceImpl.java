package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;
import com.choongang.frombirth_backend.model.entity.WeeklyReport;
import com.choongang.frombirth_backend.repository.WeeklyReportRepository;
import com.choongang.frombirth_backend.service.WeeklyReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeeklyReportServiceImpl implements WeeklyReportService {
    private final WeeklyReportRepository weeklyReportRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WeeklyReportServiceImpl(WeeklyReportRepository weeklyReportRepository, ModelMapper modelMapper) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WeeklyReportDTO> getAllReports(Integer childId) {
        return weeklyReportRepository.findByChildId(childId).stream()
                .map(report -> modelMapper.map(report, WeeklyReportDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyReportDTO getReportById(Integer reportId) {
        WeeklyReport report = weeklyReportRepository.findById(reportId).orElseThrow();
        return modelMapper.map(report, WeeklyReportDTO.class);
    }

    @Override
    public void addReport(WeeklyReportDTO weeklyReportDTO) {
        WeeklyReport report = modelMapper.map(weeklyReportDTO, WeeklyReport.class);
        report.setCreatedAt(LocalDateTime.now());
        weeklyReportRepository.save(report);
    }

    @Override
    public void deleteReport(Integer reportId) {
        weeklyReportRepository.deleteById(reportId);
    }

    // 'read' 상태 업데이트 메서드
    @Override
    public boolean updateReportReadStatus(Integer reportId, boolean isRead) {
        // 보고서를 조회
        WeeklyReport report = weeklyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // 'isRead' 값을 true로 업데이트
        report.setRead(true);  // 'isRead' 필드 업데이트

        // 업데이트된 보고서를 저장
        weeklyReportRepository.save(report);

        // 업데이트 성공
        return true;
    }

    @Override
    public Integer getAllReportCount(Integer childId) {
        return weeklyReportRepository.countByChildId(childId);
    }
}

