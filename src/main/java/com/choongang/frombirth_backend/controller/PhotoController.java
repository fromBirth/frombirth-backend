package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.PhotoDTO;
import com.choongang.frombirth_backend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/all/{recordId}")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(@PathVariable Integer recordId) {
        return ResponseEntity.ok(photoService.getAllPhotos(recordId));
    }

    @GetMapping("/record/{photoId}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Integer photoId) {
        return ResponseEntity.ok(photoService.getPhotoById(photoId));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> addPhoto(@RequestBody PhotoDTO photoDTO) {
        photoService.addPhoto(photoDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updatePhoto(@RequestBody PhotoDTO photoDTO) {
        photoService.updatePhoto(photoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Integer photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }



}
