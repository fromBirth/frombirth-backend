package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.entity.RefreshToken;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.RefreshTokenRepository;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(JwtTokenProvider jwtTokenProvider,
                          UsersRepository usersRepository,
                          RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.usersRepository = usersRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 토큰 기반 로그인 엔드포인트 (액세스 토큰만 받음)
    @PostMapping("/token-login")
    public ResponseEntity<?> tokenLogin(@RequestBody TokenLoginRequest tokenLoginRequest) {
        String accessToken = tokenLoginRequest.getAccessToken();

        try {
            // 액세스 토큰 검증
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Integer userId = jwtTokenProvider.getUserIdFromToken(accessToken);

                // 사용자 조회
                Optional<Users> optionalUser = usersRepository.findById(userId);
                if (optionalUser.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
                }

                Users user = optionalUser.get();

                // 사용자 정보 반환 (필요한 정보만 선택적으로 반환)
                return ResponseEntity.ok(Map.of(
                        "userId", user.getUserId(),
                        "email", user.getEmail()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token expired");
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    // 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 리프레시 토큰을 쿠키에서 추출 (혹은 요청 본문에서 추출)
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is required");
        }

        try {
            // 리프레시 토큰 검증
            if (jwtTokenProvider.validateToken(refreshToken)) {
                Integer userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

                // 사용자 조회
                Optional<Users> optionalUser = usersRepository.findById(userId);
                if (optionalUser.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                }

                Users user = optionalUser.get();

                // 저장된 리프레시 토큰 조회
                Optional<RefreshToken> optionalStoredRefreshToken = refreshTokenRepository.findById(userId);
                if (optionalStoredRefreshToken.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
                }

                RefreshToken storedRefreshToken = optionalStoredRefreshToken.get();

                if (!refreshToken.equals(storedRefreshToken.getToken()) ||
                        storedRefreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
                }

                // 새로운 액세스 토큰 생성
                String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

                // 새로운 액세스 토큰을 쿠키에 설정 (HttpOnly 사용 안 함)
                Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
                accessTokenCookie.setHttpOnly(false); // HttpOnly 사용 안 함
                accessTokenCookie.setSecure(true); // 프로덕션 환경에서는 true로 설정
                accessTokenCookie.setPath("/");
                accessTokenCookie.setMaxAge((int)(jwtTokenProvider.getAccessTokenValidity() / 1000)); // 30분
                response.addCookie(accessTokenCookie);

                return ResponseEntity.ok("Access token refreshed");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    // 로그아웃 엔드포인트 (선택 사항)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 refreshToken을 추출하여 삭제
        String refreshToken = null;
        Integer userId = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null) {
            try {
                userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

                // 리프레시 토큰 삭제
                Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(userId);
                optionalRefreshToken.ifPresent(refreshTokenRepository::delete);

                // 쿠키 삭제
                Cookie accessTokenCookie = new Cookie("accessToken", "");
                accessTokenCookie.setPath("/");
                accessTokenCookie.setMaxAge(0);
                response.addCookie(accessTokenCookie);

                Cookie refreshTokenCookie = new Cookie("refreshToken", "");
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setMaxAge(0);
                response.addCookie(refreshTokenCookie);

                return ResponseEntity.ok("Logout successful");

            } catch (JwtException | IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
            }
        }

        return ResponseEntity.badRequest().body("Refresh token is required");
    }

    // DTO 클래스
    @Data
    public static class TokenLoginRequest {
        private String accessToken;
    }
}
