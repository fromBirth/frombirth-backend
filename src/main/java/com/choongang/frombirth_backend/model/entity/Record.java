package com.choongang.frombirth_backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "record", indexes = @Index(name = "idx_record", columnList = "child_id, record_date", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @Column(name = "child_id", nullable = false)
    private Integer childId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "height", precision = 4, scale = 1)
    private BigDecimal height;

    @Column(name = "weight", precision = 4, scale = 1)
    private BigDecimal weight;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "video_result")
    private Integer videoResult;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false, insertable = false, updatable = false)
    private Children children;

}
