package com.choongang.frombirth_backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    public List<String> uploadPhotos(MultipartFile[] photos, String diaryId) throws IOException;
    public String uploadProfile(MultipartFile profile, Integer childId) throws IOException;
    public String modifyFilenameToUrl(String fileName);
    public void deleteFile(String fileName);
    public List<String> getFileNamesAll();
}
