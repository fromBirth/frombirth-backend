package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UsersRepository usersRepository;

    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        System.out.println("유저정보찾기시작");
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer userId = userPrincipal.getUserId();

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자 정보를 반환 (DTO로 변환하여 반환하는 것이 좋습니다)
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail()
        ));
    }
}
