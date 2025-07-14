package com.pm.campaign_service.repository;

import com.pm.campaign_service.model.Campaign;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends CrudRepository<Campaign, UUID> {
    boolean existsByName(String name);
    List<Campaign> findAllByActiveIsTrue();
}
