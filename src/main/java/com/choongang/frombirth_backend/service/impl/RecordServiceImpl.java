package com.choongang.frombirth_backend.service.impl;

import static com.choongang.frombirth_backend.service.impl.PhotoServiceImpl.*;
import static com.choongang.frombirth_backend.util.Util.getRecordFileName;

import com.choongang.frombirth_backend.model.dto.MonthRecordPhotoDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.dto.RecordDTO;
import com.choongang.frombirth_backend.model.dto.RecordPhotoDTO;
import com.choongang.frombirth_backend.model.entity.Record;
import com.choongang.frombirth_backend.repository.RecordRepository;
import com.choongang.frombirth_backend.service.PhotoService;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.S3Service;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.bytebuddy.asm.Advice.Local;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final PhotoService photoService;
    private final S3Service s3Service;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public RecordServiceImpl(RecordRepository recordRepository, PhotoService photoService, S3Service s3Service,
                             ModelMapper modelMapper,
                             RestTemplate restTemplate) {
        this.recordRepository = recordRepository;
        this.photoService = photoService;
        this.s3Service = s3Service;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RecordDTO> getAllRecords(Integer childId) {
        return recordRepository.findByChildId(childId).stream()
                .map(record -> modelMapper.map(record, RecordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordDTO> getAllRecords(Integer childId, Integer lastRecordId, Integer size, String query) {
        PageRequest pageRequest = PageRequest.of(0, size);

        System.out.println(childId);
        System.out.println(lastRecordId);

        Slice<RecordDTO> recordPage = recordRepository.getRecordPage(
                childId, lastRecordId, pageRequest, query);

        System.out.println(recordPage);

        return recordPage.getContent().stream()
                .peek(recordDTO -> recordDTO.getImages().forEach(photo -> {
                    String fileName = getRecordFileName(photo.getRecordId(), photo.getUrl());
                    photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
                }))
                .collect(Collectors.toList());
    }

    @Override
    public RecordDTO getRecordById(Integer recordId) {
        return Stream.of(recordRepository.getRecordDetail(recordId)).peek(record -> {
            record.getImages().forEach((photo -> {
                String fileName = getRecordFileName(photo.getRecordId(), photo.getUrl());
                photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
            }));
        }).findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void addRecord(RecordDTO recordDTO, MultipartFile[] images, MultipartFile video) throws IOException {
        Record record = modelMapper.map(recordDTO, Record.class);
        record.setCreatedAt(LocalDateTime.now()); // 레코드 생성 시간 설정
        record.setChildId(recordDTO.getChildId());

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

        postVideoForResult(video, record);
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

    @Override
    public List<MonthRecordPhotoDTO> getAllRecordPhoto(Integer childId, String lastMonth, Integer size, String query) {
        PageRequest pageRequest = PageRequest.of(0, size);

        // YearMonth로 변환 후 LocalDate의 첫날로 변환
        YearMonth yearMonth = YearMonth.parse(lastMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate localDate = yearMonth.atDay(1); // 해당 월의 첫날로 변환

        Slice<MonthRecordPhotoDTO> page = recordRepository.getRecordPhotoByMonth(childId, localDate, pageRequest,
                query);
        return page.getContent().stream()
                .peek(recordDTO -> recordDTO.getPhotos().forEach(photo -> {
                    String fileName = getRecordFileName(photo.getRecordId(), photo.getUrl());
                    photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
                }))
                .collect(Collectors.toList());
    }

    // 🔴 childId와 month로 데이터 조회
    @Override
    public List<RecordPhotoDTO> getRecordByIdAndMonth(Integer childId, String month) {
        List<RecordPhotoDTO> recordPhotos = recordRepository.getRecordByIdAndMonth(childId, month);

        // 조회된 데이터의 파일명을 변환
        if (recordPhotos != null) {
            recordPhotos.forEach(photo -> {
                String fileName = getRecordFileName(photo.getRecordId(), photo.getPhotoUrl());
                photo.setPhotoUrl(s3Service.modifyFilenameToUrl(fileName));
            });
        }

        return recordPhotos;
    }

    // 🔴 날짜와 childId로 데이터 조회
    @Override
    public RecordDTO getRecordByDate(Integer childId, String date) {
        RecordDTO recordDTO = recordRepository.findByChildIdAndDate(childId, date);

        // recordDTO의 이미지 리스트에서 파일명을 변경
        if (recordDTO != null && recordDTO.getImages() != null) {
            recordDTO.getImages().forEach(photo -> {
                String fileName = getRecordFileName(photo.getRecordId(), photo.getUrl());
                photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
            });
        }

        return recordDTO;
    }

    @Override
    public List<PhotoDTO> getRandomPhoto(Integer childId) {
        return recordRepository.getRandomPhotoList(childId).stream()
                .peek(photo -> {
                    String fileName = getRecordFileName(photo.getRecordId(), photo.getUrl());
                    photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAllRecordCount(Integer childId) {
        // 이번 주 월요일과 오늘 날짜 구하기
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return recordRepository.countByChildId(childId, startOfWeek, today);
    }

    @Override
    public List<RecordDTO> getRecordsByChildIdWithNonNullHeight(Integer childId, Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Record> records = recordRepository.findByChildIdAndHeightIsNotNullOrderByRecordDateDesc(childId, pageable);

        // records를 역순으로 정렬
        Collections.reverse(records);

        // Record를 RecordDTO로 변환하여 반환
        return records.stream()
                .map(record -> modelMapper.map(record, RecordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordDTO> getRecordsByChildIdAndWeightIsNotNullOrderByRecordDateDesc(Integer childId, Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Record> records = recordRepository.findByChildIdAndWeightIsNotNullOrderByRecordDateDesc(childId, pageable);

        // records를 역순으로 정렬
        Collections.reverse(records);

        // Record를 RecordDTO로 변환하여 반환
        return records.stream()
                .map(record -> modelMapper.map(record, RecordDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecordDTO getLatestRecordByChildIdWithHeightAndWeight(Integer childId) {
        Optional<Record> record = Optional.ofNullable(
                recordRepository.findFirstByChildIdAndHeightIsNotNullAndWeightIsNotNullOrderByRecordDateDesc(
                        childId));
        return record.map(value -> modelMapper.map(value, RecordDTO.class)).orElse(null);
    }

    @Value("${python.url}")
    private String pythonUrl;

    @Async
    protected void postVideoForResult(MultipartFile video, Record record) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(video.getBytes()) {
            @Override
            public String getFilename() {
                return video.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(pythonUrl + "/analysisVideo", HttpMethod.POST,
                requestEntity, String.class);

        Integer result = Integer.parseInt(Objects.requireNonNull(response.getBody()));

        record.setVideoResult(result);
        recordRepository.save(record);
    }
}
