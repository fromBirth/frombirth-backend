package com.choongang.frombirth_backend.security;

import com.choongang.frombirth_backend.model.entity.RefreshToken;
import com.choongang.frombirth_backend.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    // 생성자 주입
    public AuthService(RefreshTokenRepository refreshTokenRepository, JwtTokenProvider tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }
    public String generateAccessToken(Integer userId) {
        if (tokenProvider == null) {
            System.out.println("JwtTokenProvider is null");
            throw new IllegalStateException("JwtTokenProvider is not initialized");
        }

        if (userId == null) {
            System.out.println("userId is null");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return tokenProvider.generateAccessToken(userId);
    }

    public String generateRefreshToken(Integer userId) {
        System.out.println("AuthService-userId"+userId);
        String refreshToken = tokenProvider.generateRefreshToken(userId);

        // 리프레시 토큰 저장 또는 업데이트
        RefreshToken token = refreshTokenRepository.findById(userId).orElse(new RefreshToken());
        token.setUserId(userId);
        token.setToken(refreshToken);
        token.setExpiryDate(LocalDateTime.now().plusWeeks(1));
        refreshTokenRepository.save(token);

        return refreshToken;
    }

}
