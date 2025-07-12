package com.pm.campaign_service.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductRequestDTO {
    @NotBlank
    private String name;

    private String description;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
