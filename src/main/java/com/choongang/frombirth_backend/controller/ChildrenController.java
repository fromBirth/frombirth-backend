package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*") // 모든 출처 허용 (개발용)
public class ChildrenController {
    private final ChildrenService childrenService;

    @Autowired
    public ChildrenController(ChildrenService childrenService) {
        this.childrenService = childrenService;
    }

    @GetMapping("/all/{userId}") // UserId에 대한 아이 목록
    public ResponseEntity<List<ChildrenDTO>> getAllChildren(@PathVariable Integer userId) {
        return ResponseEntity.ok(childrenService.getAllChildren(userId));
    }


    @GetMapping("/child/{childId}") // 아이 개별 확인 get방식
    public ResponseEntity<ChildrenDTO> getChildById(@PathVariable Integer childId) {
        return ResponseEntity.ok(childrenService.getChildById(childId));
    }

    @PostMapping("/create") //아이 프로필 생성
    public ResponseEntity<Void> addChild(@Valid @RequestBody ChildrenDTO childrenDTO) {
        childrenService.addChild(childrenDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
