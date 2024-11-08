package com.choongang.frombirth_backend;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.PhotoService;
import com.choongang.frombirth_backend.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class S3Test {

    @Autowired
    S3Client s3Client;

    @Autowired
    PhotoService photoService;

    @Autowired
    ChildrenService childrenService;

    @Autowired
    S3Service s3Service;

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

    // Url 변환 확인 테스트 코드
    @Test
    public void getObject() {
        // PhotoDTO URL 확인
//        List<PhotoDTO> list = photoService.getAllPhotosByRecordId(13);
//        System.out.println(list.toString());
    }

    @Test
    public void getChildProfileUrl() {
        // 아이 프로필 사진 url 확인
//        ChildrenDTO dto =  childrenService.getChildById(38);
//        System.out.println(dto);
    }

    // S3 동기화 테스트 코드
    @Test
    public void deleteObject() {
//        // S3에서 모든 파일명 가져오기
//        List<String> s3Filenames = s3Service.getFileNamesAll();
//        //System.out.println(s3Filenames.toString());
//
//        // 모든 Photo 가져오기
//        List<PhotoDTO> photoList = photoService.getAllPhotos();
//        photoList.forEach( photo -> photo.setUrl("record/" + photo.getRecordId() + "/" + photo.getUrl()));
//        //System.out.println(photoList.toString());
//
//        List<String> photoListFilenames = photoList.stream().map(PhotoDTO::getUrl).collect(Collectors.toList());
//        System.out.println(photoListFilenames);
//
//        // 모든 children 가져오기
//        List<ChildrenDTO> childrenList = childrenService.getAllChildren();
//        childrenList = childrenList.stream().filter(child -> child.getProfilePicture() != null).collect(Collectors.toList());
//        childrenList.forEach( child -> child.setProfilePicture("children/" + child.getChildId() + "/" + child.getProfilePicture()));
//        //System.out.println(childrenList.toString());
//
//        List<String> childrenListFilenames = childrenList.stream().map(ChildrenDTO::getProfilePicture).collect(Collectors.toList());
//        System.out.println(childrenListFilenames);
//
//        // S3에 존재하지만 Photo에 없는 파일 찾기
//        List<String> s3RecordFilenames = s3Filenames.stream().filter(file -> file.startsWith("record/")).collect(Collectors.toList());
//        List<String> s3ProfileFilenames = s3Filenames.stream().filter(file -> file.startsWith("children/")).collect(Collectors.toList());
//        System.out.println(s3RecordFilenames.toString());
//        System.out.println(s3ProfileFilenames.toString());
//
//        s3RecordFilenames.forEach(file -> {
//            if(!photoListFilenames.contains(file)) {
//                s3Service.deleteFile(file);
//            }
//        });
//        s3ProfileFilenames.forEach(file -> {
//            if(!childrenListFilenames.contains(file)) {
//                s3Service.deleteFile(file);
//            }
//        });


    }

}
