package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDTO createOrGetUser(UserDTO userDTO);
    UserDTO getUserById(Integer userId);
    UserDTO updateUser(Integer userId, UserDTO userDTO);
    void deleteUser(Integer userId);
    List<UserDTO> getAllUsers();



    boolean isAccessTokenValid(String accessToken);
    Map<String, String> refreshAccessToken(String refreshToken);
    UserDTO getUserByAccessToken(String accessToken);
}

