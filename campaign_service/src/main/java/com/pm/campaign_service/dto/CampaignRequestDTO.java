package com.pm.campaign_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CampaignRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String product;

    @NotEmpty
    private List<String> keywords;

    @NotEmpty
    private double bid_amount;

    private double campaign_amount;

    @NotBlank
    private String city;

    private int radius;

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

    public @NotBlank String getProduct() {
        return product;
    }

    public void setProduct(@NotBlank String product) {
        this.product = product;
    }

    public @NotEmpty List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(@NotEmpty List<String> keywords) {
        this.keywords = keywords;
    }

    @NotEmpty
    public double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(@NotEmpty double bid_amount) {
        this.bid_amount = bid_amount;
    }

    public double getCampaign_amount() {
        return campaign_amount;
    }

    public void setCampaign_amount(double campaign_amount) {
        this.campaign_amount = campaign_amount;
    }

    public @NotBlank String getCity() {
        return city;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
