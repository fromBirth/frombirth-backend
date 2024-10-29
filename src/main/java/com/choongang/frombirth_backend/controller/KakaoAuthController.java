package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
public class KakaoAuthController {

    @Value("${kakao.REST-API-key}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final UserService userService;

    @Autowired
    public KakaoAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        // 카카오 서버에 access token 요청
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", "http://172.30.1.18:8181/api/kakao/callback");
        requestBody.add("code", code);
        requestBody.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            String accessToken = (String) body.get("access_token");

            // access token을 사용해 사용자 정보 요청
            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.add("Authorization", "Bearer " + accessToken);

            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = userInfoResponse.getBody();

                System.out.println(userInfo);
                // 카카오 사용자 정보를 기반으로 UserDTO 생성
                UserDTO userDTO = new UserDTO();
                userDTO.setKakaoId(Long.parseLong(userInfo.get("id").toString()));
                userDTO.setEmail(((Map<String, Object>) userInfo.get("kakao_account")).get("email").toString());

                // 사용자 정보를 데이터베이스에 저장하거나, 기존 사용자 정보 반환
                UserDTO registeredUser = userService.createUser(userDTO);

                return ResponseEntity.ok(registeredUser);
            } else {
                return ResponseEntity.status(userInfoResponse.getStatusCode()).body("사용자 정보 요청 실패");
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("토큰 요청 실패");
        }
    }
}
