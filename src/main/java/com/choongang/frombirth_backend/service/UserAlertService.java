package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.UserAlertDTO;

import java.util.List;

public interface UserAlertService {
    List<UserAlertDTO> getAllAlerts(String userId);
    UserAlertDTO getAlertById(Integer notificationId);
    void addAlert(UserAlertDTO userAlertDTO);
    void updateAlert(UserAlertDTO userAlertDTO);
    void deleteAlert(Integer notificationId);
}
