package com.choongang.frombirth_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


        private final long accessTokenValidity = 1000L * 60 * 30; // 30분
        private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    public String generateAccessToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e; // 만료된 토큰 예외는 따로 처리하기 위해 다시 던집니다.
        } catch (JwtException | IllegalArgumentException e) {
            // 그 외의 예외는 false 반환
            return false;
        }
    }

    // 토큰에서 사용자 ID 추출
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }
    // 액세스 토큰의 유효 기간 반환
    public long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    // 리프레시 토큰의 유효 기간 반환
    public long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }
}
