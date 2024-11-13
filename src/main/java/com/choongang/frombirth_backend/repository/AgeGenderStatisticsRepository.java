package com.choongang.frombirth_backend.repository;

import com.choongang.frombirth_backend.model.entity.AgeGenderStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeGenderStatisticsRepository extends JpaRepository<AgeGenderStatistics, Integer> {
    AgeGenderStatistics findByGenderAndAge(String gender, Integer age);
}
