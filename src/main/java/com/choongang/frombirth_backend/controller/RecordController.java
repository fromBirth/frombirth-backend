package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<RecordDTO>> getAllRecords(@PathVariable Integer childId) {
        return ResponseEntity.ok(recordService.getAllRecords(childId));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Integer recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }

    @PostMapping
    public ResponseEntity<Void> addRecord(@RequestBody RecordDTO recordDTO) {
        recordService.addRecord(recordDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateRecord(@RequestBody RecordDTO recordDTO) {
        recordService.updateRecord(recordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Integer recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}
