package com.pm.auth_service.mapper;

import com.pm.auth_service.dto.UserResponseDTO;
import com.pm.auth_service.model.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(user.getId().toString());
        userDTO.setRole(user.getRole());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }
}
