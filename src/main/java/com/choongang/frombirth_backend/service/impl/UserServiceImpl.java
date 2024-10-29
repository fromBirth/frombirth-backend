package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUsername(), user.getPasswordHash(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(String userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        return new UserDTO(user.getUserId(), user.getUsername(), user.getPasswordHash(), user.getCreatedAt(), user.getUpdatedAt());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Users newUser = new Users();
        newUser.setUserId(userDTO.getUserId());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPasswordHash(userDTO.getPasswordHash());
        usersRepository.save(newUser);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPasswordHash(userDTO.getPasswordHash());
        usersRepository.save(existingUser);
        return userDTO;
    }

    @Override
    public void deleteUser(String userId) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        usersRepository.delete(existingUser);
    }
}
