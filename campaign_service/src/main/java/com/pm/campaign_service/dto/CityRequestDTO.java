package com.pm.campaign_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CityRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private double latitude;   // y

    @NotNull
    private double longitude;  // x

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    @NotNull
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull double latitude) {
        this.latitude = latitude;
    }

    @NotNull
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull double longitude) {
        this.longitude = longitude;
    }
}
