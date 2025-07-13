package com.pm.auth_service.service;

import com.pm.auth_service.exception.AuthOperationException;
import com.pm.auth_service.exception.UserOperationException;
import com.pm.auth_service.model.User;
import com.pm.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void save(User newUser) {
        userRepository.save(newUser);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
