package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(@PathVariable Integer recordId) {
        return ResponseEntity.ok(photoService.getAllPhotos(recordId));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Integer photoId) {
        return ResponseEntity.ok(photoService.getPhotoById(photoId));
    }

    @PostMapping
    public ResponseEntity<Void> addPhoto(@RequestBody PhotoDTO photoDTO) {
        photoService.addPhoto(photoDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> updatePhoto(@RequestBody PhotoDTO photoDTO) {
        photoService.updatePhoto(photoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Integer photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }



}
