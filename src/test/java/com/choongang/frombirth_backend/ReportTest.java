package com.choongang.frombirth_backend;

import com.choongang.frombirth_backend.model.dto.WeeklyReportDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.RecordService;
import com.choongang.frombirth_backend.service.WeeklyReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ReportTest {

    @Autowired
    private ChildrenService childrenService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeeklyReportService weeklyReportService;

    @Value("${cloud.aws.lambda.url}")
    private String baseUrl;

    @Value("${cloud.aws.x-api-key}")
    private String xApiKey;

    // 전체 childId 에 대해 주간 보고 생성
//    @Test
//    public void createReportTest() {
//
//        // weekly_report 테이블에 해당 내용 등록
//        // 필요 값 : childId, risk_level, feedback
//
//        // 1. childId 를 모두 가져옴
//        List<Integer> list = childrenService.getAllChildIds();
//        //System.out.println(list.toString());
//
//        // 2. 각 childId 별 저번주 일기를 모두 가져옴
//        list.forEach(childId -> {
//            List<String> diaryContent = recordService.getRecordContentWeekly(childId);
//            System.out.println(diaryContent.toString());
//
//            // 일기 데이터가 4개 이상인 경우만 문항 검진
//            if(!diaryContent.isEmpty()){
//                //System.out.println(diaryContent);
//
//                String analyzeUrl = baseUrl + "/analyze";
//                String adviceUrl = baseUrl + "/advice";
//
//                // JSON 요청 본문을 위한 Map 생성
//                Map<String, Object> requestPayload = new HashMap<>();
//                requestPayload.put("diary_content", diaryContent);
//
//                // HttpHeaders 설정
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.set("x-api-key", xApiKey); // x-api-key 값 설정
//
//                // HttpEntity 생성
//                HttpEntity<Object> entity = new HttpEntity<>(requestPayload, headers);
//
//                // 3. list 로 만든 후 json 으로 보냄.
//                // REST API 요청 및 응답 처리
//                try {
//                    // 4-1. feedback 컬럼
//                    // json 객체로 조언 받음 조언을 feedback 으로 저장
//                    String feedback = doAIReport("advice", adviceUrl, entity, String.class);
//                    System.out.println("Feedback: " + feedback);
//
//                    // 4-2. riskLevel 컬럼
//                    // totalMatches 로 일치 항목 개수를 받음.
//                    // 개수를 5단계로 나눠 위험도 저장 Integer 값 1 2 3 4 5
//                    Integer riskLevel = null;
//                    if(diaryContent.size() > 3) {
//                        Integer totalMatches = doAIReport("total_matches", analyzeUrl, entity, Integer.class);
//                        riskLevel = calculateRiskLevel(totalMatches);
//
//                        //System.out.println("Total Matches: " + totalMatches + " Risk Level: " + riskLevel);
//                    }
//
//                    // 5. WeeklyReport 엔티티에 저장
//                    WeeklyReportDTO dto = WeeklyReportDTO.builder()
//                            .childId(childId)
//                            .feedback(feedback)
//                            .riskLevel(riskLevel)
//                            .build();
//
//                    System.out.println(dto.toString());
//                    //weeklyReportService.addReport(dto);
//
//                } catch (Exception e) {
//                    System.err.println("Error for childId " + childId + ": " + e.getMessage());
//                }
//
//            } else {
//               // 일기가 없으면 report 생성 안함
//               System.out.println(diaryContent.size() + "개. 개수 부족");
//            }
//        });
//
//
//    }

    // 임의의 childId 값에 대해 주간 보고 생성
//    @Test
//    void createReportTest02() {
//
//        Integer childId = 71;
//        List<String> diaryContent = recordService.getRecordContentWeekly(childId);
//
//        String analyzeUrl = baseUrl + "/analyze";
//        String adviceUrl = baseUrl + "/advice";
//
//        Map<String, Object> requestPayload = new HashMap<>();
//        requestPayload.put("diary_content", diaryContent);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("x-api-key", xApiKey); // x-api-key 값 설정
//
//        HttpEntity<Object> entity = new HttpEntity<>(requestPayload, headers);
//
//        try {
//            String feedback = doAIReport("advice", adviceUrl, entity, String.class);
//            System.out.println("Feedback: " + feedback);
//
//            Integer riskLevel = null;
//            if(diaryContent.size() > 3) {
//                Integer totalMatches = doAIReport("total_matches", analyzeUrl, entity, Integer.class);
//                riskLevel = calculateRiskLevel(totalMatches);
//            }
//
//            WeeklyReportDTO dto = WeeklyReportDTO.builder()
//                    .childId(childId)
//                    .feedback(feedback)
//                    .riskLevel(riskLevel)
//                    .build();
//
//            System.out.println(dto.toString());
//            weeklyReportService.addReport(dto);
//
//        } catch (Exception e) {
//            System.err.println("Error for childId " + childId + ": " + e.getMessage());
//        }
//    }

    private <T> T doAIReport(String key, String url, HttpEntity<Object> entity, Class<T> responseType) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        String responseBody = response.getBody();

        // 응답 내용 파싱
        if (responseBody != null) {
            System.out.println("Full Response Body: " + responseBody); // 전체 응답 출력

            JsonNode root = objectMapper.readTree(responseBody);
            String innerBody = root.path("body").asText(); // body 필드를 문자열로 추출
            JsonNode innerBodyJson = objectMapper.readTree(innerBody); // innerBody를 JSON으로 파싱
            int statusCode = root.path("statusCode").asInt(); // root에서 statusCode를 추출

            System.out.println("Status Code: " + statusCode);
            System.out.println("Inner Body JSON: " + innerBodyJson.toString());

            if (statusCode == 200) {
                JsonNode keyNode = innerBodyJson.path(key);

                // 키가 존재하고 값이 설정되어 있는지 확인
                if (!keyNode.isMissingNode() && !keyNode.isNull()) {
                    return objectMapper.treeToValue(keyNode, responseType);
                } else {
                    System.err.println("Warning: Key '" + key + "' not found or is null in response.");
                    return null;
                }

            } else if (statusCode == 400 || statusCode == 500) {
                String errorMessage = innerBodyJson.path("error").asText();
                System.err.println("Warning: " + statusCode + ": " + errorMessage);
                return null;
            }

        } else {
            System.err.println("Response body is null");
        }

        return null;
    }


    private Integer calculateRiskLevel(Integer totalMatches) {
        if (totalMatches <= 1) {
            return 1; // 정상
        } else if (totalMatches <= 3) {
            return 2; // 경미한 주의
        } else if (totalMatches <= 5) {
            return 3; // 주의
        } else if (totalMatches <= 7) {
            return 4; // 높은 주의
        } else {
            return 5; // 고위험
        }
    }
}
