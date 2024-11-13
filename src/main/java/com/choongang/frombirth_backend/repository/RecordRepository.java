package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import java.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface RecordRepository extends
        JpaRepository<Record, Integer>,
        RecordCustomRepository,
        QuerydslPredicateExecutor<Record> {
    List<Record> findByChildId(Integer childId); // 특정 자녀의 기록 목록 가져오기
    List<Record> findByChildIdAndHeightIsNotNullOrderByRecordDateDesc(Integer childId, Pageable pageable); //특정 자녀의 신장이 null이 아닌 최근 pageable만큼의 기록 가져오기
    List<Record> findByChildIdAndWeightIsNotNullOrderByRecordDateDesc(Integer childId, Pageable pageable); //특정 자녀의 몸무게가 null이 아닌 최근 pageable만큼의 기록 가져오기

    @Query("SELECT COUNT(record) FROM Record record where record.childId = :childId AND record.recordDate between :startOfWeek and :today")
    Integer countByChildId(
            @Param("childId")Integer childId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("today") LocalDate today
    );

    Record findFirstByChildIdAndHeightIsNotNullAndWeightIsNotNullOrderByRecordDateDesc(Integer childId);

}
