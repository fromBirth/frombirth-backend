package com.choongang.frombirth_backend.model.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "age_gender_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeGenderStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_gender_statistics_id")
    private Integer ageGenderStatisticsId;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "avg_height", nullable = false)
    private Double avgHeight;

    @Column(name = "avg_weight", nullable = false)
    private Double avgWeight;
}
