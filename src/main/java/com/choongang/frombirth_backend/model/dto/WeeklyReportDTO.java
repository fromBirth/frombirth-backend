package com.choongang.frombirth_backend.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyReportDTO {
    private Integer reportId;
    private Integer childId;
    private Integer riskLevel;
    private String feedback;
    private LocalDateTime createdAt;
    private boolean isRead;
}

