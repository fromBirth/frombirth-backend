package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.AgeGenderStatisticsDTO;
import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.entity.AgeGenderStatistics;
import com.choongang.frombirth_backend.repository.AgeGenderStatisticsRepository;
import com.choongang.frombirth_backend.service.AgeGenderStatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AgeGenderStatisticsServiceImpl implements AgeGenderStatisticsService {

    private final AgeGenderStatisticsRepository ageGenderStatisticsRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public AgeGenderStatisticsServiceImpl(AgeGenderStatisticsRepository ageGenderStatisticsRepository, ModelMapper modelMapper) {
        this.ageGenderStatisticsRepository = ageGenderStatisticsRepository;
        this.modelMapper = modelMapper;
    }


    public AgeGenderStatisticsDTO getAverageStatistics(ChildrenDTO childrenDTO) {
        LocalDate birthDate = childrenDTO.getBirthDate();
        String gender = childrenDTO.getGender();

        // 출생 개월 수 계산
        int months = (int)Period.between(birthDate, LocalDate.now()).toTotalMonths();

        System.out.println("개월수"+months);
        System.out.println("성별"+gender);

        // 성별과 개월 수로 평균 키와 몸무게 조회
        AgeGenderStatistics ageGenderStatistics =ageGenderStatisticsRepository.findByGenderAndAge(gender, months);
        if(ageGenderStatistics == null){
            return null;
        }
        return  modelMapper.map(ageGenderStatistics, AgeGenderStatisticsDTO.class);
    }
}
