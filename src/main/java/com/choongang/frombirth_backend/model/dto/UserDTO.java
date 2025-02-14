package com.choongang.frombirth_backend.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private Long kakaoId;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

