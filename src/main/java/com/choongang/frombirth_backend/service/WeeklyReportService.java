package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;

import java.util.List;

public interface WeeklyReportService {
    List<WeeklyReportDTO> getAllReports(Integer childId);          // 모든 보고서 조회
    WeeklyReportDTO getReportById(Integer reportId);               // 특정 보고서 조회
    void addReport(WeeklyReportDTO weeklyReportDTO);               // 보고서 추가
    void deleteReport(Integer reportId);                            // 보고서 삭제
    boolean updateReportReadStatus(Integer reportId, boolean isRead);  // 보고서 읽음 상태 업데이트
    Integer getAllReportCount(Integer childId);
}
