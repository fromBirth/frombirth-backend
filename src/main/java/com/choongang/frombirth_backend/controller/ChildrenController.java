package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.AgeGenderStatisticsDTO;
import com.choongang.frombirth_backend.service.AgeGenderStatisticsService;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class ChildrenController {
    private final ChildrenService childrenService;
    private final S3Service s3Service;
    private final RecordService recordService;
    private final AgeGenderStatisticsService ageGenderStatisticsService;

    @Autowired
    public ChildrenController(ChildrenService childrenService, S3Service s3Service, RecordService recordService, AgeGenderStatisticsService ageGenderStatisticsService) {
        this.childrenService = childrenService;
        this.s3Service = s3Service;
        this.recordService = recordService;
        this.ageGenderStatisticsService = ageGenderStatisticsService;
    }

    @GetMapping("/all/{userId}") // UserId에 대한 아이 목록
    public ResponseEntity<List<ChildrenDTO>> getAllChildren(@PathVariable Integer userId) {
        return ResponseEntity.ok(childrenService.getAllChildrenByUserId(userId));
    }


    @GetMapping("/child/{childId}") // 아이 개별 확인 get방식
    public ResponseEntity<ChildrenDTO> getChildById(@PathVariable Integer childId) {
        return ResponseEntity.ok(childrenService.getChildById(childId));
    }

    @PostMapping("/create") //아이 프로필 생성
    public ResponseEntity<ChildrenDTO> addChild(
             @RequestPart ChildrenDTO childrenDTO
           ,@RequestPart(value = "profile", required = false) MultipartFile profile) {

        System.out.println(childrenDTO);

        try {
            ChildrenDTO result = childrenService.addChild(childrenDTO, profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update") // 아이 프로필 업데이트
    public ResponseEntity<Void> updateChild(@RequestBody ChildrenDTO childrenDTO) {
        childrenService.updateChild(childrenDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{childId}") // 아이 프로필 삭제
    public ResponseEntity<Void> deleteChild(@PathVariable Integer childId) {
        childrenService.deleteChild(childId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/height-data/{childId}")
    public ResponseEntity<Map<String, Object>> getHeightData(@PathVariable Integer childId) {
        System.out.println("키 데이터 받기");
        System.out.println("childId: " + childId);

        ChildrenDTO childrenDTO = childrenService.getChildById(childId);
        List<RecordDTO> recordHeightList = recordService.getRecordsByChildIdWithNonNullHeight(childId, 7);
        List<RecordDTO> recordWeightList =recordService.getRecordsByChildIdAndWeightIsNotNullOrderByRecordDateDesc(childId, 7);

        for (RecordDTO record : recordHeightList) {
            System.out.println("기록값"+ record);
        }
        AgeGenderStatisticsDTO statistics = ageGenderStatisticsService.getAverageStatistics(childrenDTO);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> heightData = new ArrayList<>();
        List<Map<String, Object>> weightData = new ArrayList<>();

        Map<String, Object> initialData = new HashMap<>();
        initialData.put("date", "1001-01-01");
        initialData.put("height", null);
        heightData.add(initialData);

        Map<String, Object> initialData2 = new HashMap<>();
        initialData2.put("date", "1001-01-01");
        initialData2.put("weight", null);
        weightData.add(initialData2);

        // 만약 기록이 7개 미만이라면 출생일 데이터를 추가
        if (recordHeightList.size() < 7) {
            Map<String, Object> birthData = new HashMap<>();
            LocalDate birthDate = childrenDTO.getBirthDate();
            if (birthDate != null) {
                birthData.put("date", birthDate);
            } else {
                birthData.put("date", "빈값");
            }

            birthData.put("height", childrenDTO.getBirthHeight());
            heightData.add(birthData);
        }
        if (recordWeightList.size() < 7) {
            Map<String, Object> birthData = new HashMap<>();
            LocalDate birthDate = childrenDTO.getBirthDate();
            if (birthDate != null) {
                birthData.put("date", birthDate);
            } else {
                birthData.put("date", "빈값");
            }

            birthData.put("weight", childrenDTO.getBirthWeight());
            weightData.add(birthData);
        }


        // 최근 기록을 추가
        for (RecordDTO record : recordHeightList) {
            Map<String, Object> recordData = new HashMap<>();

            LocalDate recordDate = record.getRecordDate();
            if (recordDate != null) {
                recordData.put("date", recordDate);
            } else {
                recordData.put("date", "빈값");
            }

            recordData.put("height", record.getHeight());
            heightData.add(recordData);
        }

        for (RecordDTO record : recordWeightList) {
            Map<String, Object> recordData = new HashMap<>();

            LocalDate recordDate = record.getRecordDate();
            if (recordDate != null) {
                recordData.put("date", recordDate);
            } else {
                recordData.put("date", "빈값");
            }

            recordData.put("weight", record.getWeight());
            weightData.add(recordData);
        }

        // 추가적인 빈값 데이터
        Map<String, Object> f = new HashMap<>();
        f.put("date", "9999-12-31");
        f.put("height", null);
        heightData.add(f);

        Map<String, Object> w = new HashMap<>();
        w.put("date", "9999-12-31");
        w.put("weight", null);
        weightData.add(w);
        if (statistics != null) {
            Map<String, Object> statisticsData = new HashMap<>();
            statisticsData.put("avgHeight", statistics.getAvgHeight());
            statisticsData.put("avgWeight", statistics.getAvgWeight());
            response.put("statistics", statisticsData);
        }

        response.put("heightData", heightData);
        response.put("weightData", weightData);
        return ResponseEntity.ok(response);
    }
}
