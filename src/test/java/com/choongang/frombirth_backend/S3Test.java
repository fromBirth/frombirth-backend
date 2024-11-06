package com.choongang.frombirth_backend;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.List;

@SpringBootTest
public class S3Test {

    @Autowired
    S3Client s3Client;

    @Autowired
    PhotoService photoService;

    @Autowired
    ChildrenService childrenService;

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

    @Test
    public void getObject() {
        // PhotoDTO URL 확인
        List<PhotoDTO> list = photoService.getAllPhotos(13);
        System.out.println(list.toString());
    }

    @Test
    public void getChildProfileUrl() {
        // 아이 프로필 사진 url 확인
        ChildrenDTO dto =  childrenService.getChildById(38);
        System.out.println(dto);
    }

}
