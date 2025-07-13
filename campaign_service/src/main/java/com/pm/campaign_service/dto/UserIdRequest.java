package com.pm.campaign_service.dto;

import jakarta.validation.constraints.NotBlank;

public class UserIdRequest {
    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
