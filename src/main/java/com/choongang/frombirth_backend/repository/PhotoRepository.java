package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findByRecordId(Integer recordId); // 특정 기록의 사진 목록 가져오기
}
