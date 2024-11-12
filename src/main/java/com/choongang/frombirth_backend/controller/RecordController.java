package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.service.RecordService;
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
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
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

    @GetMapping("/child/{recordId}") // 아이의 기록 불러오기
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Integer recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }

    @GetMapping("/all/{childId}/{month}")
    public ResponseEntity<List<RecordPhotoDTO>> getAllRecordsByMonth(@PathVariable Integer childId,
                                                                     @PathVariable String month) {
        return ResponseEntity.ok(recordService.getRecordByIdAndMonth(childId, month));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    //아이 기록 생성
    public ResponseEntity<Void> addRecord(@RequestPart RecordDTO recordDTO,
                                          @RequestPart(value = "images", required = false) MultipartFile[] images,
                                          @RequestPart(value = "video", required = false) MultipartFile video) {

        System.out.println(recordDTO.toString());

        try {
            // RecordDTO와 파일을 서비스로 전달
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
}
