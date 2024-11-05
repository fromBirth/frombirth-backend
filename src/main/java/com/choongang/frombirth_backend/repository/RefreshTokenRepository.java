package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(Integer userId);
    void save(Integer userId);
}
