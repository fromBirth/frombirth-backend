package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Integer> {
    List<WeeklyReport> findByChildId(Integer childId); // 특정 자녀의 주간 보고서 목록 가져오기
    // 특정 childId에 대한 보고서 개수를 계산
    int countByChildId(Integer childId);
}
