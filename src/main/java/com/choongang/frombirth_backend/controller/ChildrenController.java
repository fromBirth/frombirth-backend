package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import com.choongang.frombirth_backend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class ChildrenController {
    private final ChildrenService childrenService;
    private final S3Service s3Service;

    @Autowired
    public ChildrenController(ChildrenService childrenService, S3Service s3Service) {
        this.childrenService = childrenService;
        this.s3Service = s3Service;
    }

    @GetMapping("/all/{userId}") // UserId에 대한 아이 목록
    public ResponseEntity<List<ChildrenDTO>> getAllChildren(@PathVariable Integer userId) {
        return ResponseEntity.ok(childrenService.getAllChildrenByUserId(userId));
    }


    @GetMapping("/child/{childId}") // 아이 개별 확인 get방식
    public ResponseEntity<ChildrenDTO> getChildById(@PathVariable Integer childId) {
        return ResponseEntity.ok(childrenService.getChildById(childId));
    }

    @PostMapping("/create") //아이 프로필 생성
    public ResponseEntity<ChildrenDTO> addChild(
             @RequestPart ChildrenDTO childrenDTO
           ,@RequestPart(value = "profile", required = false) MultipartFile profile) {

        System.out.println(childrenDTO);

        try {
            ChildrenDTO result = childrenService.addChild(childrenDTO, profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update") // 아이 프로필 업데이트
    public ResponseEntity<Void> updateChild(@RequestBody ChildrenDTO childrenDTO) {
        childrenService.updateChild(childrenDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{childId}") // 아이 프로필 삭제
    public ResponseEntity<Void> deleteChild(@PathVariable Integer childId) {
        childrenService.deleteChild(childId);
        return ResponseEntity.noContent().build();
    }

}
