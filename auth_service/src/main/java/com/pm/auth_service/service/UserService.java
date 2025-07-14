package com.pm.auth_service.service;

import com.pm.auth_service.dto.UserResponseDTO;
import com.pm.auth_service.exception.AuthOperationException;
import com.pm.auth_service.exception.UserOperationException;
import com.pm.auth_service.mapper.UserMapper;
import com.pm.auth_service.model.User;
import com.pm.auth_service.repository.UserRepository;
import com.pm.auth_service.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserOperationException("User not found"));
    }
    public User findById(String id) {
        return userRepository.findById(UuidUtil.parseUuidOrThrow(id))
                .orElseThrow(() -> new UserOperationException("User not found"));
    }


    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<UserResponseDTO> findAll() {
        Iterable<User> users = userRepository.findAll();

        return StreamSupport.stream(users.spliterator(), false).map(UserMapper::toDTO).toList();
    }
}
