package com.pm.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank(message = "email is required")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password at least 8 characters long")
    private String password;

    public @NotBlank(message = "email is required") @Email(message = "email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "email is required") @Email(message = "email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "password is required") @Size(min = 8, message = "password at least 8 characters long") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password is required") @Size(min = 8, message = "password at least 8 characters long") String password) {
        this.password = password;
    }
}

