package com.pm.stats_service.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID campaignId;

    private long clicks;
    private double spentAmount;

    public UUID getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(UUID id) {
        this.campaignId = id;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }
}
