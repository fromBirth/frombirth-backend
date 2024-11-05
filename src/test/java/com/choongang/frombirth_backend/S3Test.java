package com.choongang.frombirth_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

@SpringBootTest
public class S3Test {

    @Autowired
    S3Client s3Client;

    @Test
    public void test() {
//        String key = "파일명으로 테스트";
//
//        // 기존 메타데이터 가져오기
//        HeadObjectRequest headRequest = HeadObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//        HeadObjectResponse headResponse = s3Client.headObject(headRequest);
//
//        // 객체 복사하여 Content-Type과 Content-Disposition 설정 변경
//        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
//                .sourceBucket(bucketName)
//                .sourceKey(key)
//                .destinationBucket(bucketName)
//                .destinationKey(key)
//                .contentType("image/png")  // 적절한 이미지 MIME 타입
//                .metadataDirective("REPLACE")
//                .contentDisposition("inline") // 브라우저에서 이미지로 표시
//                .build();
//
//        s3Client.copyObject(copyRequest);
//        System.out.println("Content-Type and Content-Disposition updated for: " + key);
//
//        s3Client.close();
    }

}
