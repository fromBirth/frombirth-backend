package com.choongang.frombirth_backend.service.impl;

import com.choongang.frombirth_backend.model.dto.UserDTO;
import com.choongang.frombirth_backend.model.entity.Users;
import com.choongang.frombirth_backend.repository.UsersRepository;
import com.choongang.frombirth_backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        return new UserDTO(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Optional<Users> existingUser = usersRepository.findByKakaoId(userDTO.getKakaoId());

        if (existingUser.isPresent()) {
            return modelMapper.map(existingUser.get(), UserDTO.class);
        } else {
            // 유저가 없으면 새로운 유저 생성 후 저장
            Users newUser = modelMapper.map(userDTO, Users.class);
            Users savedUser = usersRepository.save(newUser);

            // 저장된 유저를 UserDTO로 변환하여 반환
            return modelMapper.map(savedUser, UserDTO.class);
        }
    }

    @Override
    public UserDTO updateUser(Integer userId, UserDTO userDTO) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        usersRepository.save(existingUser);
        return userDTO;
    }

    @Override
    public void deleteUser(Integer userId) {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        usersRepository.delete(existingUser);
    }
}
