package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import com.choongang.frombirth_backend.repository.RecordRepository;
import com.choongang.frombirth_backend.service.PhotoService;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final PhotoService photoService;
    private final S3Service s3Service;
    private final ModelMapper modelMapper;

    @Autowired
    public RecordServiceImpl(RecordRepository recordRepository, PhotoService photoService, S3Service s3Service, ModelMapper modelMapper) {
        this.recordRepository = recordRepository;
        this.photoService = photoService;
        this.s3Service = s3Service;
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
    @Transactional
    public void addRecord(RecordDTO recordDTO, MultipartFile[] images, MultipartFile video) throws IOException {
        Record record = modelMapper.map(recordDTO, Record.class);
        record.setCreatedAt(LocalDateTime.now()); // 레코드 생성 시간 설정

        // Record 저장
        Record savedRecord = recordRepository.save(record);
        Integer recordId = savedRecord.getRecordId();

        // 이미지 업로드
        List<String> photoUrls = new ArrayList<>();
        if (images != null) {
            photoUrls = s3Service.uploadPhotos(images, String.valueOf(record.getRecordId()));
        }

        // URL 저장
        for (String photoUrl : photoUrls) {
            PhotoDTO photoDTO = PhotoDTO.builder()
                    .recordId(recordId)
                    .url(photoUrl)
                    .createdAt(LocalDateTime.now())
                    .build();
            photoService.addPhoto(photoDTO);
        }
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
