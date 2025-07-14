package com.pm.auth_service.service;

import com.pm.auth_service.dto.LoginRequestDTO;
import com.pm.auth_service.dto.RegisterRequestDTO;
import com.pm.auth_service.dto.UserRequestDTO;
import com.pm.auth_service.dto.UserResponseDTO;
import com.pm.auth_service.exception.AuthOperationException;
import com.pm.auth_service.exception.UserOperationException;
import com.pm.auth_service.mapper.UserMapper;
import com.pm.auth_service.model.User;
import com.pm.auth_service.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(LoginRequestDTO loginRequestDTO) {

        User user = userService.findByEmail(loginRequestDTO.getEmail());

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword()))
            throw new AuthOperationException("Wrong password");

        return jwtUtil.generateToken(user.getId().toString(), user.getRole());
    }

    public String register(RegisterRequestDTO registerRequestDTO) {

        if (userService.existsByEmail(registerRequestDTO.getEmail()))
            throw new UserOperationException("Email already in use");

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole("USER");

        userService.save(user);
        return jwtUtil.generateToken(user.getId().toString(), user.getRole());
    }

    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        User user = userService.findById(id);

        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(userRequestDTO.getRole());

        User saved = userService.save(user);

        return UserMapper.toDTO(saved);
    }


    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


}
