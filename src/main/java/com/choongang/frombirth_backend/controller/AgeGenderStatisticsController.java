package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.AgeGenderStatisticsDTO;
import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.service.AgeGenderStatisticsService;
import com.choongang.frombirth_backend.service.ChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistic")
@CrossOrigin(origins = "*")
public class AgeGenderStatisticsController {
    private final AgeGenderStatisticsService ageGenderStatisticsService;
    private final ChildrenService childrenService;
    @Autowired
    public AgeGenderStatisticsController(AgeGenderStatisticsService ageGenderStatisticsService, ChildrenService childrenService) {
        this.ageGenderStatisticsService = ageGenderStatisticsService;
        this.childrenService = childrenService;
    }


    @GetMapping("/average/{childId}")
    public ResponseEntity<AgeGenderStatisticsDTO> getAverageStatistics(@PathVariable Integer childId){
        System.out.println("ㅇ"+childId);
        ChildrenDTO children = childrenService.getChildById(childId);
        AgeGenderStatisticsDTO averageStatistics = ageGenderStatisticsService.getAverageStatistics(children);
        return ResponseEntity.ok(averageStatistics);
    }

}
