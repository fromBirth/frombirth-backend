package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.S3Service;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService, S3Service s3Service) {
        this.recordService = recordService;
    }

    @GetMapping("/all/{childId}") // 아이의 전체 기록 불러오기
    public ResponseEntity<List<RecordDTO>> getAllRecords(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getAllRecords(childId));
    }

    @GetMapping("/cnt/{childId}")
    public ResponseEntity<Integer> getAllRecordCount(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getAllRecordCount(childId));
    }

    @GetMapping("/cnt/{childId}/week")
    public ResponseEntity<Integer> getAllRecordCountWeekly(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getAllRecordCountWeekly(childId));
    }

    @GetMapping(value = {
            "/all/{childId}/{lastRecordId}/{size}/{query}",
            "/all/{childId}/{lastRecordId}/{size}"
    }) // 아이의 전체 기록 불러오기
    public ResponseEntity<List<RecordDTO>> getAllRecords(@PathVariable Integer childId,
                                                         @PathVariable Integer lastRecordId,
                                                         @PathVariable Integer size,
                                                         @PathVariable(required = false) String query) {
        System.out.println("get All diary start");
        System.out.println(query);
        return ResponseEntity.ok(recordService.getAllRecords(childId, lastRecordId, size, query));
    }

    @GetMapping(value = {"/all/photo/{childId}/{lastMonth}/{size}", "/all/photo/{childId}/{lastMonth}/{size}/{query}"})
    // 아이의 전체 기록 불러오기
    public ResponseEntity<List<MonthRecordPhotoDTO>> getAllRecordPhoto(@PathVariable Integer childId,
                                                                       @PathVariable String lastMonth,
                                                                       @PathVariable Integer size,
                                                                       @PathVariable(required = false) String query) {
        System.out.println("get All diary photo start");
        System.out.println(query);
        System.out.println(childId);
        System.out.println(lastMonth);
        return ResponseEntity.ok(recordService.getAllRecordPhoto(childId, lastMonth, size, query));
    }

    @GetMapping("/child/{recordId}") // 특정 기록 ID로 아이의 기록 불러오기
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Integer recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }

    @GetMapping("/all/{childId}/{month}") // 월별 기록 불러오기
    public ResponseEntity<List<RecordPhotoDTO>> getAllRecordsByMonth(@PathVariable Integer childId, @PathVariable String month) {
        return ResponseEntity.ok(recordService.getRecordByIdAndMonth(childId, month));
    }

    @GetMapping("/randomPhoto/{childId}")
    public ResponseEntity<List<PhotoDTO>> getRandomPhoto(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getRandomPhoto(childId));
    }

    // 날짜별로 특정 아이의 기록 불러오기
    @GetMapping("/child/{childId}/date/{date}")
    public ResponseEntity<RecordDTO> getRecordByDate(@PathVariable Integer childId, @PathVariable String date) {
        try {
            RecordDTO recordDTO = recordService.getRecordByDate(childId, date);
            if (recordDTO != null) {
                return ResponseEntity.ok(recordDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    //아이 기록 생성
    public ResponseEntity<Void> addRecord(@RequestPart RecordDTO recordDTO,
                                          @RequestPart(value = "images", required = false) MultipartFile[] images,
                                          @RequestPart(value = "video", required = false) MultipartFile video) {

        System.out.println(recordDTO.toString());

        try {
            recordService.addRecord(recordDTO, images, video);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateRecord(@RequestBody RecordDTO recordDTO) {
        recordService.updateRecord(recordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Integer recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/growth-data-last/{childId}")
    public ResponseEntity<RecordDTO> getGrowthData(@PathVariable Integer childId) {
        RecordDTO record = recordService.getLatestRecordByChildIdWithHeightAndWeight(childId);

        // record가 null일 경우 404 응답 반환
        return ResponseEntity.ok(Objects.requireNonNullElseGet(record, RecordDTO::new));
    }

    @GetMapping("/growth-data-count/{childId}")
    public ResponseEntity<Integer> getGrowthDataCount(@PathVariable Integer childId) {
        // record가 null일 경우 404 응답 반환
        return ResponseEntity.ok(recordService.countRecordsWithNonNullHeightAndWeightByChildId(childId));
    }
}
