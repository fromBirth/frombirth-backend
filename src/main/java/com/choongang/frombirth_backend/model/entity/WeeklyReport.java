package com.choongang.frombirth_backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weekly_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Integer reportId;

    @Column(name = "child_id", nullable = false)
    private Integer childId;

    @Column(name = "risk_level")
    private Integer riskLevel;

    @Column(name = "feedback", length = 100)
    private String feedback;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
