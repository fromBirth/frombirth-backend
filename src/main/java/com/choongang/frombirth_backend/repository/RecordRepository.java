package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findByChildId(Integer childId); // 특정 자녀의 기록 목록 가져오기
}
