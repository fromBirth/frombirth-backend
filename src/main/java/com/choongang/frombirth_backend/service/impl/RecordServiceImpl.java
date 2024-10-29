package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import com.choongang.frombirth_backend.repository.RecordRepository;
import com.choongang.frombirth_backend.service.RecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        recordRepository.save(record);
    }

    @Override
    public void updateRecord(RecordDTO recordDTO) {
        Record record = modelMapper.map(recordDTO, Record.class);
        recordRepository.save(record);
    }

    @Override
    public void deleteRecord(Integer recordId) {
        recordRepository.deleteById(recordId);
    }
}
