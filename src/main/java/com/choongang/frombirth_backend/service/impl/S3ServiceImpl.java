package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3ServiceImpl(S3Client s3Client) { this.s3Client = s3Client; }

    public List<String> uploadPhotos(MultipartFile[] photos, String diaryId) throws IOException {
        List<String> photoUrls = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String rootPath = "record/" + diaryId;
            String photoUrl = doUpload(rootPath, photo);
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }

    @Override
    public String uploadProfile(MultipartFile profile, Integer childId) throws IOException {
        String rootPath = "children/" + childId;
        return doUpload(rootPath, profile);
    }

    private String doUpload(String rootPath, MultipartFile photo) throws IOException {
        String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
        String path = rootPath + "/" + fileName;
        String contentType = determineContentType(Objects.requireNonNull(photo.getOriginalFilename()));
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(path)
                        .contentType(contentType)           // 이미지 형식에 맞는 Content-Type 설정
                        .contentDisposition("inline")       // 브라우저에서 직접 표시되도록 설정
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(photo.getBytes()));

        return fileName;
    }

    private String determineContentType(String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public String modifyFilenameToUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    @Override
    public List<String> getFileNamesAll() {
        List<String> s3Filenames = new ArrayList<>();
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        for (S3Object s3Object : response.contents()) {
            s3Filenames.add(s3Object.key());
        }

        return s3Filenames;
    }

    @Override
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        DeleteObjectResponse deleteResponse = s3Client.deleteObject(deleteRequest);
        System.out.println(deleteResponse.toString());
    }
}
