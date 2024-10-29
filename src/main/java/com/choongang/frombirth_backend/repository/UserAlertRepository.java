package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAlertRepository extends JpaRepository<UserAlert, Integer> {
    List<UserAlert> findByUserId(String userId); // 특정 사용자의 알림 목록 가져오기
}
