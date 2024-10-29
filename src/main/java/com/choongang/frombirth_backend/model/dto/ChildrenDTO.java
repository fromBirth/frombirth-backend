package com.choongang.frombirth_backend.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenDTO {
    private Integer childId;
    private Integer userId;

    @NotNull(message = "이름은 필수입니다.")
    private String name;

    private Integer order;
    private LocalDate birthDate;
    private String gender;
    private String bloodType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer birthWeight;
    private LocalTime birthTime;
}

