package com.pm.campaign_service.dto;

import com.pm.campaign_service.dto.validator.CreateCampaignValidationGroup;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CampaignRequestDTO {

    @NotBlank(groups = CreateCampaignValidationGroup.class, message = "Name is required")
    private String name;

    private String description;

    @NotBlank(groups = CreateCampaignValidationGroup.class, message = "Product is required")
    private String product;

    private List<String> keywords;

    @NotEmpty(groups = CreateCampaignValidationGroup.class, message = "Bid amount is required")
    @DecimalMin(value = "0.01")
    private double bid_amount;

    @DecimalMin(value = "0.00")
    private double campaign_amount;

    @NotBlank(groups = CreateCampaignValidationGroup.class, message = "City is required")
    private String city;

    @Min(0)
    private int radius;

    public @NotBlank(groups = CreateCampaignValidationGroup.class, message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(groups = CreateCampaignValidationGroup.class, message = "Name is required") String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotBlank(groups = CreateCampaignValidationGroup.class, message = "Product is required") String getProduct() {
        return product;
    }

    public void setProduct(@NotBlank(groups = CreateCampaignValidationGroup.class, message = "Product is required") String product) {
        this.product = product;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @NotEmpty(groups = CreateCampaignValidationGroup.class, message = "Bid amount is required")
    @DecimalMin(value = "0.01")
    public double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(@NotEmpty(groups = CreateCampaignValidationGroup.class, message = "Bid amount is required") @DecimalMin(value = "0.01") double bid_amount) {
        this.bid_amount = bid_amount;
    }

    @DecimalMin(value = "0.00")
    public double getCampaign_amount() {
        return campaign_amount;
    }

    public void setCampaign_amount(@DecimalMin(value = "0.00") double campaign_amount) {
        this.campaign_amount = campaign_amount;
    }

    public @NotBlank(groups = CreateCampaignValidationGroup.class, message = "City is required") String getCity() {
        return city;
    }

    public void setCity(@NotBlank(groups = CreateCampaignValidationGroup.class, message = "City is required") String city) {
        this.city = city;
    }

    @Min(0)
    public int getRadius() {
        return radius;
    }

    public void setRadius(@Min(0) int radius) {
        this.radius = radius;
    }
}
