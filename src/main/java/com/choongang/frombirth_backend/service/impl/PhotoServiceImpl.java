package com.choongang.frombirth_backend.service.impl;

import static com.choongang.frombirth_backend.util.Util.getRecordFileName;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.model.entity.Photo;
import com.choongang.frombirth_backend.repository.PhotoRepository;
import com.choongang.frombirth_backend.service.PhotoService;
import com.choongang.frombirth_backend.service.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, ModelMapper modelMapper, S3Service s3Service) {
        this.photoRepository = photoRepository;
        this.modelMapper = modelMapper;
        this.s3Service = s3Service;
    }

    @Override
    public List<PhotoDTO> getAllPhotosByRecordId(Integer recordId) {
        return photoRepository.findByRecordId(recordId).stream()
                .map(photo -> {
                    String fileName = getRecordFileName(recordId, photo.getUrl());
                    photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
                    return modelMapper.map(photo, PhotoDTO.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAll().stream()
                .map(photo -> modelMapper.map(photo, PhotoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhotoDTO getPhotoById(Integer photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow();
        String fileName = photo.getUrl();
        photo.setUrl(s3Service.modifyFilenameToUrl(fileName));
        return modelMapper.map(photo, PhotoDTO.class);
    }

    @Override
    public void addPhoto(PhotoDTO photoDTO) {
        Photo photo = modelMapper.map(photoDTO, Photo.class);
        photo.setCreatedAt(LocalDateTime.now());
        photoRepository.save(photo);
    }

    @Override
    public void updatePhoto(PhotoDTO photoDTO) {
        Photo photo = modelMapper.map(photoDTO, Photo.class);
        photo.setCreatedAt(LocalDateTime.now());
        photoRepository.save(photo);
    }

    @Override
    public void deletePhoto(Integer photoId) {
        photoRepository.deleteById(photoId);
    }



}
