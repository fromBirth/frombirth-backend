package com.choongang.frombirth_backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3UploadService {
    public List<String> uploadPhotos(MultipartFile[] photos, String diaryId) throws IOException;
    public String uploadProfile(MultipartFile profile, Integer userId) throws IOException;
}
