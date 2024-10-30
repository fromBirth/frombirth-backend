package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import com.choongang.frombirth_backend.repository.RecordRepository;
import com.choongang.frombirth_backend.service.RecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecordServiceImpl(RecordRepository recordRepository, ModelMapper modelMapper) {
        this.recordRepository = recordRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RecordDTO> getAllRecords(Integer childId) {
        return recordRepository.findByChildId(childId).stream()
                .map(record -> modelMapper.map(record, RecordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecordDTO getRecordById(Integer recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow();
        return modelMapper.map(record, RecordDTO.class);
    }

    @Override
    public void addRecord(RecordDTO recordDTO) {
        Record record = modelMapper.map(recordDTO, Record.class);
        record.setCreatedAt(LocalDateTime.now()); // 레코드 생성 시간 설정
        recordRepository.save(record);
    }

    @Override
    public void updateRecord(RecordDTO recordDTO) {
        Record existingRecord = recordRepository.findById(recordDTO.getRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        // height가 null이 아닐 때만 업데이트
        if (recordDTO.getHeight() != null) {
            existingRecord.setHeight(recordDTO.getHeight());
        }
        // weight가 null이 아닐 때만 업데이트
        if (recordDTO.getWeight() != null) {
            existingRecord.setWeight(recordDTO.getWeight());
        }
        // content가 null이 아닐 때만 업데이트
        if (recordDTO.getContent() != null) {
            existingRecord.setContent(recordDTO.getContent());
        }
        // videoResult가 null이 아닐 때만 업데이트
        if (recordDTO.getVideoResult() != null) {
            existingRecord.setVideoResult(recordDTO.getVideoResult());
        }

        // 마지막 수정 시간 설정
        existingRecord.setUpdatedAt(LocalDateTime.now());

        recordRepository.save(existingRecord);
    }


    @Override
    public void deleteRecord(Integer recordId) {
        recordRepository.deleteById(recordId);
    }
}
