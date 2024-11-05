package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.model.entity.RefreshToken;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.RefreshTokenRepository;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.security.AuthService;
import com.choongang.frombirth_backend.security.JwtTokenProvider;
import com.choongang.frombirth_backend.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${FRONTEND_URL}")
    private String frontEndUrl;

    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final AuthService authService;

    public AuthController(JwtTokenProvider jwtTokenProvider,
                          UsersRepository usersRepository,
                          RefreshTokenRepository refreshTokenRepository, UserService userService, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.usersRepository = usersRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.authService = authService;
    }
    @GetMapping("/test-login")
    public void testLogin(@RequestParam Integer userId, HttpServletResponse response) throws IOException {
        System.out.println("테스트 로그인 시작");

        // userId로 사용자 정보 조회
        UserDTO userDTO = userService.getUserById(userId);
        if (userDTO == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "해당 유저가 없습니다");
            return;
        }

        // JWT 액세스 토큰과 리프레시 토큰 생성
        String accessToken = authService.generateAccessToken(userDTO.getUserId());
        String refreshToken = authService.generateRefreshToken(userDTO.getUserId());

        // 토큰을 일반 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        // 리디렉트 URL
        String redirectUrl = frontEndUrl + "/testLogin";
        response.sendRedirect(redirectUrl);
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
        System.out.println("리프레시토큰 재발급");
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
                accessTokenCookie.setPath("/");
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
