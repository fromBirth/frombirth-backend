package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username); // 사용자 이름으로 사용자 찾기
}
