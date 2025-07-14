package com.pm.auth_service.controller;

import com.pm.auth_service.dto.UserRequestDTO;
import com.pm.auth_service.dto.UserResponseDTO;
import com.pm.auth_service.exception.UserOperationException;
import com.pm.auth_service.service.AuthService;
import com.pm.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(userService.findAll());
        } else {
            throw new UserOperationException("User not authorized");
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<UserResponseDTO> editUser(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @RequestBody UserRequestDTO userRequestDTO
    ){

        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(authService.updateUser(id, userRequestDTO));
        } else {
            throw new UserOperationException("User not authorized");
        }
    }

    // TODO user deletion (it has to delete all campaign assigned to user)
}
