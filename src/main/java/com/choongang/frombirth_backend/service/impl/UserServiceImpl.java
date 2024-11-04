package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${kakao.REST-API-key}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;


    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper, RestTemplate restTemplate) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        return new UserDTO(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

    }

    @Override
    public UserDTO createOrGetUser(UserDTO userDTO) {
        Optional<Users> optionalUser = usersRepository.findByKakaoId(userDTO.getKakaoId());

        if (optionalUser.isEmpty()) {
            Users newUser = modelMapper.map(userDTO, Users.class);
            Users savedUser = usersRepository.save(newUser);

            // 저장된 유저를 UserDTO로 변환하여 반환
            return modelMapper.map(savedUser, UserDTO.class);
        }

        Users user = optionalUser.get();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Integer userId, UserDTO userDTO) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        usersRepository.save(existingUser);
        return userDTO;
    }

    @Override
    public void deleteUser(Integer userId) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        usersRepository.delete(existingUser);
    }

    @Override
    public boolean isAccessTokenValid(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";
        HttpHeaders headers = new HttpHeaders();
        System.out.println(accessToken+"ㅁ");
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return true; // 토큰이 유효하면 true 반환
        }catch (RestClientResponseException e) {
            // 에러 상태 코드와 메시지 콘솔 출력
            System.out.println("카카오 API 호출 실패");
            System.out.println("HTTP 상태 코드: " + e.getRawStatusCode());
            System.out.println("에러 메시지: " + e.getResponseBodyAsString());
            return false; // 유효하지 않은 토큰으로 간주
        } catch (Exception e) {
            System.out.println("예상치 못한 에러 발생: " + e.getMessage());
            return false;
        }
    }
    @Override
    public Map<String, String> refreshAccessToken(String refreshToken) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("client_id", clientId);
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("access_token")) {
            // 액세스 토큰과, 리프레시 토큰이 있으면 둘 다 반환
            String newAccessToken = (String) body.get("access_token");
            String newRefreshToken = (String) body.getOrDefault("refresh_token", refreshToken);

            // 반환값으로 액세스 토큰과 리프레시 토큰을 함께 전달
            return Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", newRefreshToken
            );
        } else {
            throw new RuntimeException("새로운 액세스 토큰 발급 실패");
        }
    }


    @Override
    public UserDTO getUserByAccessToken(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> userInfo = response.getBody();

            if (userInfo != null) {
                String kakaoId = userInfo.get("id").toString();
                String email = ((Map<String, Object>) userInfo.get("kakao_account")).get("email").toString();
                System.out.println("kakaoId:" + kakaoId);
                System.out.println("email:" + email);

                Users user = usersRepository.findByKakaoId(Long.parseLong(kakaoId))
                        .orElseGet(() -> {
                            Users newUser = new Users();
                            newUser.setKakaoId(Long.parseLong(kakaoId));
                            newUser.setEmail(email);
                            return usersRepository.save(newUser);
                        });

                return modelMapper.map(user, UserDTO.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }

        throw new RuntimeException("사용자 정보 조회 실패");
    }
}
