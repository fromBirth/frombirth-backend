package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;

import java.util.List;

public interface WeeklyReportService {
    List<WeeklyReportDTO> getAllReports(Integer childId);
    WeeklyReportDTO getReportById(Integer reportId);
    void addReport(WeeklyReportDTO weeklyReportDTO);
    void updateReport(WeeklyReportDTO weeklyReportDTO);
    void deleteReport(Integer reportId);
}
