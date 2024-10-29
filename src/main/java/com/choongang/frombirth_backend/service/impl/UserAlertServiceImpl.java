package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.UserAlertDTO;
import com.choongang.frombirth_backend.model.entity.UserAlert;
import com.choongang.frombirth_backend.repository.UserAlertRepository;
import com.choongang.frombirth_backend.service.UserAlertService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAlertServiceImpl implements UserAlertService {
    private final UserAlertRepository userAlertRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserAlertServiceImpl(UserAlertRepository userAlertRepository, ModelMapper modelMapper) {
        this.userAlertRepository = userAlertRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserAlertDTO> getAllAlerts(Integer userId) {
        return userAlertRepository.findByUserId(userId).stream()
                .map(alert -> modelMapper.map(alert, UserAlertDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserAlertDTO getAlertById(Integer notificationId) {
        UserAlert alert = userAlertRepository.findById(notificationId).orElseThrow();
        return modelMapper.map(alert, UserAlertDTO.class);
    }

    @Override
    public void addAlert(UserAlertDTO userAlertDTO) {
        UserAlert alert = modelMapper.map(userAlertDTO, UserAlert.class);
        userAlertRepository.save(alert);
    }

    @Override
    public void updateAlert(UserAlertDTO userAlertDTO) {
        UserAlert alert = modelMapper.map(userAlertDTO, UserAlert.class);
        userAlertRepository.save(alert);
    }

    @Override
    public void deleteAlert(Integer notificationId) {
        userAlertRepository.deleteById(notificationId);
    }
}
