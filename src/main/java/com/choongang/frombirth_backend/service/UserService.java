package com.choongang.frombirth_backend.service;

import com.choongang.frombirth_backend.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Integer userId);
    UserDTO updateUser(Integer userId, UserDTO userDTO);
    void deleteUser(Integer userId);
    List<UserDTO> getAllUsers();

}

