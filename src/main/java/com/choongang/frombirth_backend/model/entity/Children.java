package com.choongang.frombirth_backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Children {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id", nullable = false)
    private Integer childId;

    @Column(name = "user_id", nullable = false, length = 50)
    private Integer userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "birth_weight", precision = 4, scale = 1)  // 전체 4자리, 소수점 이하 1자리
    private BigDecimal birthWeight;

    @Column(name = "birth_height", precision = 4, scale = 1)
    private BigDecimal birthHeight;

    @Column(name = "birth_time")
    private LocalTime birthTime;

    @Column(name = "profile_picture")
    private String profilePicture; // 아기 대표 사진

//    @OneToMany(mappedBy = "children", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Record> records = new ArrayList<>();

}
