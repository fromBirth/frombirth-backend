package com.choongang.frombirth_backend.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {
    private Integer recordId;
    private Integer childId;
    private LocalDate recordDate;
    private Float height;
    private Float weight;
    private String content;
    private Integer videoResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
