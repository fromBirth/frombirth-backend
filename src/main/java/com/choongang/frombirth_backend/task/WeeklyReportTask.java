package com.choongang.frombirth_backend.task;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.WeeklyReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class WeeklyReportTask {

    // 실행 로깅
    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportTask.class);

    // 리스크 값 설정
    private static final int NORMAL = 1;
    private static final int SLIGHT_WARNING = 2;
    private static final int WARNING = 3;
    private static final int HIGH_WARNING = 4;
    private static final int HIGH_RISK = 5;

    // 최소 일기 개수
    private static final int MIN_SIZE = 3;

    private final ChildrenService childrenService;
    private final RecordService recordService;
    private final WeeklyReportService weeklyReportService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${cloud.aws.lambda.url}")
    private String baseUrl;

    @Value("${cloud.aws.x-api-key}")
    private String xApiKey;


    @Autowired
    public WeeklyReportTask(
            ChildrenService childrenService,
            RecordService recordService,
            WeeklyReportService weeklyReportService,
            ObjectMapper objectMapper,
            RestTemplate restTemplate
    ) {
        this.childrenService = childrenService;
        this.recordService = recordService;
        this.weeklyReportService = weeklyReportService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul") // 매 주 월요일 0 시 자동 실행
    //@Scheduled(cron = "0 14 12 * * THU", zone = "Asia/Seoul") // 테스트용 값 설정 (ex 매 주 목요일 12시 14분)
    @Transactional
    public void createWeeklyReport() {
        logger.info("Create WeeklyReport...");
        List<Integer> childIds = childrenService.getAllChildIds();

        childIds.parallelStream().forEach(childId -> {
            List<String> diaryContent = recordService.getRecordContentWeekly(childId);
            logger.info("Diary content for child {}: {}", childId, diaryContent);

            boolean isEnoughForRiskLevel = diaryContent.size() > MIN_SIZE;

            // 일기 데이터가 4개 이상인 경우만 문항 검진
            if(!diaryContent.isEmpty()){
                String analyzeUrl = baseUrl + "/analyze";
                String adviceUrl = baseUrl + "/advice";

                // JSON 요청 본문을 위한 Map 생성
                Map<String, Object> requestPayload = new HashMap<>();
                requestPayload.put("diary_content", diaryContent);

                // HttpHeaders 설정
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("x-api-key", xApiKey); // x-api-key 값 설정

                // HttpEntity 생성
                HttpEntity<Object> entity = new HttpEntity<>(requestPayload, headers);

                // REST API 요청 및 응답 처리
                // feedback 컬럼
                Optional<String> feedbackOpt = doAIReport("advice", adviceUrl, entity, String.class);
                String feedback = feedbackOpt.orElse("No feedback available");

                // riskLevel 컬럼
                // totalMatches 로 일치 항목 개수를 받음.
                // 개수를 5단계로 나눠 위험도 저장 Integer 값 1 2 3 4 5
                Integer riskLevel = null;
                if (isEnoughForRiskLevel) {
                    Optional<Integer> totalMatchesOpt = doAIReport("total_matches", analyzeUrl, entity, Integer.class);
                    riskLevel = totalMatchesOpt.map(this::calculateRiskLevel).orElse(null);
                }

                // WeeklyReport 엔티티에 저장
                WeeklyReportDTO dto = WeeklyReportDTO.builder()
                        .childId(childId)
                        .feedback(feedback)
                        .riskLevel(riskLevel)
                        .build();

                weeklyReportService.addReport(dto);
                logger.info("Weekly report created for child {} with risk level {}", childId, riskLevel);

            } else {
                // 일기가 없으면 report 생성 안함
                logger.info("Not enough diary content for child {} to create report.", childId);
            }
        });

    }

    // Rest 통신 설정
    private <T> Optional<T> doAIReport(String key, String url, HttpEntity<Object> entity, Class<T> responseType) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            String responseBody = response.getBody();

            if (responseBody != null) {
                JsonNode root = objectMapper.readTree(responseBody);
                int statusCode = root.path("statusCode").asInt();
                String innerBody = root.path("body").asText();

                if (statusCode == 200) {
                    JsonNode innerBodyJson = objectMapper.readTree(innerBody);
                    JsonNode keyNode = innerBodyJson.path(key);

                    if (!keyNode.isMissingNode() && !keyNode.isNull()) {
                        return Optional.of(objectMapper.treeToValue(keyNode, responseType));
                    } else {
                        logger.warn("Key '{}' not found or is null in response.", key);
                    }
                } else {
                    String errorMessage = root.path("error").asText();
                    logger.error("HTTP Error {}: {}", statusCode, errorMessage);
                }
            } else {
                logger.error("Response body is null");
            }
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response for key '{}': {}", key, e.getMessage());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP Error during AI report request: {}", e.getMessage());
        } catch (ResourceAccessException e) {
            logger.error("Network Error during AI report request: {}", e.getMessage());
        }

        return Optional.empty();
    }

    // riskLevel 설정
    private Integer calculateRiskLevel(Integer totalMatches) {
        if (totalMatches <= 1) return NORMAL;
        else if (totalMatches <= 3) return SLIGHT_WARNING;
        else if (totalMatches <= 5) return WARNING;
        else if (totalMatches <= 7) return HIGH_WARNING;
        else return HIGH_RISK;
    }

}
