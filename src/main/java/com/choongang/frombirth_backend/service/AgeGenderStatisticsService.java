package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.AgeGenderStatisticsDTO;
import com.choongang.frombirth_backend.model.dto.ChildrenDTO;

public interface AgeGenderStatisticsService {
    AgeGenderStatisticsDTO getAverageStatistics(ChildrenDTO childrenDTO);
}
