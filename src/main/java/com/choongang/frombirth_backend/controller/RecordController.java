package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/all/{childId}") // 아이의 전체 기록 불러오기
    public ResponseEntity<List<RecordDTO>> getAllRecords(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getAllRecords(childId));
    }

    @GetMapping("/child/{recordId}") // 아이의 기록 불러오기
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Integer recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }

    @PostMapping("/create") //아이 기록 생성
    public ResponseEntity<Void> addRecord(@RequestBody RecordDTO recordDTO) {
        recordService.addRecord(recordDTO);
        return ResponseEntity.status(201).build();
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
