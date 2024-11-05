package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.security.AuthService;
import com.choongang.frombirth_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
public class KakaoAuthController {

    @Value("${kakao.rest-api-key}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${FRONTEND_URL}")
    private String frontEndUrl;

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public KakaoAuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 카카오 서버에 access token 요청
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);
        requestBody.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = responseEntity.getBody();
            String kakaoAccessToken = (String) body.get("access_token");
            String kakaoRefreshToken = (String) body.get("refresh_token");

            // access token을 사용해 사용자 정보 요청
            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.add("Authorization", "Bearer " + kakaoAccessToken);

            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = userInfoResponse.getBody();

                // 카카오 사용자 정보를 기반으로 UserDTO 생성
                UserDTO userDTO = new UserDTO();
                userDTO.setKakaoId(Long.parseLong(userInfo.get("id").toString()));
                userDTO.setEmail(((Map<String, Object>) userInfo.get("kakao_account")).get("email").toString());

                // 사용자 정보를 데이터베이스에 저장하거나, 기존 사용자 정보 반환
                UserDTO registeredUser = userService.createOrGetUser(userDTO);

                // JWT 액세스 토큰과 리프레시 토큰 생성
                String accessToken = authService.generateAccessToken(registeredUser.getUserId());
                String refreshToken = authService.generateRefreshToken(registeredUser.getUserId());

                // 액세스 토큰을 일반 쿠키에 저장
                Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
                accessTokenCookie.setPath("/");
                response.addCookie(accessTokenCookie);

                // 리프레시 토큰을 일반 쿠키에 저장
                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                refreshTokenCookie.setPath("/");
                response.addCookie(refreshTokenCookie);


                // 리다이렉트 URL에 사용자 정보와 토큰을 추가
                String redirectUrl = frontEndUrl+"/login";
                response.sendRedirect(redirectUrl);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "사용자 정보 요청 실패");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "토큰 요청 실패");
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String accessToken,
                                           @RequestHeader("Refresh-Token") String refreshToken) {
        UserDTO user;
        String newAccessToken = accessToken;
        String newRefreshToken = refreshToken;
        System.out.println("검증시작");
        if (userService.isAccessTokenValid(accessToken)) {
            user = userService.getUserByAccessToken(accessToken);
        } else {
            Map<String, String> tokens = userService.refreshAccessToken(refreshToken);
            newAccessToken = tokens.get("accessToken");
            newRefreshToken = tokens.get("refreshToken");
            user = userService.getUserByAccessToken(newAccessToken);
        }

        // 항상 동일한 구조로 응답 반환
        return ResponseEntity.ok(Map.of(
                "user", user,
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }
}
