package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByKakaoId(Long kakaoId); // 카카오 아이디로 찾기
}
