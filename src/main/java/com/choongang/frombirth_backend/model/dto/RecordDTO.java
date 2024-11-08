package com.choongang.frombirth_backend.model.dto;

import java.util.List;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RecordDTO {
    private Integer recordId;
    private Integer childId;
    private LocalDate recordDate;
    private Float height;
    private Float weight;
    private String title;
    private String content;
    private Integer videoResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
