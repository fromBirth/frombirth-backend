package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;

import java.util.List;

public interface PhotoService {
    List<PhotoDTO> getAllPhotosByRecordId(Integer recordId);
    List<PhotoDTO> getAllPhotos();
    PhotoDTO getPhotoById(Integer photoId);
    void addPhoto(PhotoDTO photoDTO);
    void updatePhoto(PhotoDTO photoDTO);
    void deletePhoto(Integer photoId);

}
