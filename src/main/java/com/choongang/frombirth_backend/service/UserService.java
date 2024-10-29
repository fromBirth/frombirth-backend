package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(String userId);
    UserDTO updateUser(String userId, UserDTO userDTO);
    void deleteUser(String userId);
    List<UserDTO> getAllUsers();

}

