package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.RecordDTO;

import java.util.List;

public interface RecordService {
    List<RecordDTO> getAllRecords(Integer childId);
    RecordDTO getRecordById(Integer recordId);
    void addRecord(RecordDTO recordDTO);
    void updateRecord(RecordDTO recordDTO);
    void deleteRecord(Integer recordId);
}
