package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.security.UserPrincipal;
import com.choongang.frombirth_backend.service.ChildrenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ChildrenService childrenService;

    @Autowired
    public UserController(UsersRepository usersRepository, ChildrenService childrenService) {
        this.usersRepository = usersRepository;
        this.childrenService = childrenService;
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

        List<ChildrenDTO> childList = childrenService.getAllChildrenByUserId(userId);
        System.out.println(childList);

        // 사용자 정보를 반환 (DTO로 변환하여 반환하는 것이 좋습니다)
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail()
        ));
    }
}
