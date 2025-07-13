package com.pm.campaign_service.repository;

import com.pm.campaign_service.model.CampaignOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignOwnershipRepository extends JpaRepository<CampaignOwnership, UUID> {
    Iterable<CampaignOwnership> findByUserId(UUID userId);
    boolean existsByCampaign_IdAndUserId(UUID campaign_id, UUID userId);
}

