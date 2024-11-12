package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecordService {
    List<RecordDTO> getAllRecords(Integer childId, Integer lastRecordId, Integer size, String query);
    RecordDTO getRecordById(Integer recordId);
    void addRecord(RecordDTO recordDTO, MultipartFile[] images, MultipartFile video) throws IOException;
    void updateRecord(RecordDTO recordDTO);
    void deleteRecord(Integer recordId);
    List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month);

    List<MonthRecordPhotoDTO> getAllRecordPhoto(Integer childId, String lastMonth, Integer size, String query);

    // 날짜와 childId로 특정 날짜의 기록 조회
    RecordDTO getRecordByDate(Integer childId, String date);

    List<RecordDTO> getRecordsByChildIdWithNonNullHeight(Integer childId,Integer limit);
    List<RecordDTO> getRecordsByChildIdAndWeightIsNotNullOrderByRecordDateDesc(Integer childId, Integer limit);
}
