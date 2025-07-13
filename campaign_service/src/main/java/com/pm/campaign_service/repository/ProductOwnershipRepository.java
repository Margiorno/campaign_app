package com.pm.campaign_service.repository;

import com.pm.campaign_service.model.CampaignOwnership;
import com.pm.campaign_service.model.ProductOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductOwnershipRepository extends JpaRepository<ProductOwnership, UUID> {
    Iterable<ProductOwnership> findByUserId(UUID userId);
    boolean existsByProduct_IdAndUserId(UUID product_id, UUID userId);
    void deleteByProductId(UUID campaignId);
}

