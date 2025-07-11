package com.pm.campaign_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private Product product;

    @NotEmpty
    @ElementCollection
    private List<String> keywords = new ArrayList<>();

    @NotNull
    @DecimalMin(value = "0.01")
    private double bid_amount;

    @NotNull
    @DecimalMin(value = "0.01")
    private double campaign_amount;

    @NotNull
    private boolean active;

    @NotBlank
    private String city;

    @NotNull
    private int radius;

    @NotNull
    private LocalDateTime created_at;

    @NotNull
    private LocalDateTime updated_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public @NotNull Product getProduct() {
        return product;
    }

    public void setProduct(@NotNull Product product) {
        this.product = product;
    }

    public @NotEmpty List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(@NotEmpty List<String> keywords) {
        this.keywords = keywords;
    }

    @NotNull
    @DecimalMin(value = "0.01")
    public double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(@NotNull @DecimalMin(value = "0.01") double bid_amount) {
        this.bid_amount = bid_amount;
    }

    @NotNull
    @DecimalMin(value = "0.01")
    public double getCampaign_amount() {
        return campaign_amount;
    }

    public void setCampaign_amount(@NotNull @DecimalMin(value = "0.01") double campaign_amount) {
        this.campaign_amount = campaign_amount;
    }

    @NotNull
    public boolean isActive() {
        return active;
    }

    public void setActive(@NotNull boolean active) {
        this.active = active;
    }

    public @NotBlank String getCity() {
        return city;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    @NotNull
    public int getRadius() {
        return radius;
    }

    public void setRadius(@NotNull int radius) {
        this.radius = radius;
    }

    public @NotNull LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(@NotNull LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public @NotNull LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(@NotNull LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
