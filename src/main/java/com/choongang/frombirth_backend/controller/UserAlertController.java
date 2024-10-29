package com.choongang.frombirth_backend.controller;

import com.choongang.frombirth_backend.model.dto.UserAlertDTO;
import com.choongang.frombirth_backend.service.UserAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class UserAlertController {
    private final UserAlertService userAlertService;

    @Autowired
    public UserAlertController(UserAlertService userAlertService) {
        this.userAlertService = userAlertService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserAlertDTO>> getAllAlerts(@PathVariable String userId) {
        return ResponseEntity.ok(userAlertService.getAllAlerts(userId));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<UserAlertDTO> getAlertById(@PathVariable Integer notificationId) {
        return ResponseEntity.ok(userAlertService.getAlertById(notificationId));
    }

    @PostMapping
    public ResponseEntity<Void> addAlert(@RequestBody UserAlertDTO userAlertDTO) {
        userAlertService.addAlert(userAlertDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAlert(@RequestBody UserAlertDTO userAlertDTO) {
        userAlertService.updateAlert(userAlertDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Integer notificationId) {
        userAlertService.deleteAlert(notificationId);
        return ResponseEntity.noContent().build();
    }
}
