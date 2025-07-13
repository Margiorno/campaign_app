package com.pm.stats_service.repository;

import com.pm.stats_service.model.Stats;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StatsRepository extends CrudRepository<Stats, UUID> {
    boolean existsByCampaignId(UUID campaignId);
    Optional<Stats> findByCampaignId(UUID campaignId);
    void deleteByCampaignId(UUID campaignId);
}
