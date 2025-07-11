package com.pm.campaign_service.service;

import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.mapper.CampaignMapper;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CampaignService {
    private CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<CampaignResponseDTO> findAll() {
        Iterable<Campaign> campaigns = campaignRepository.findAll();

        return StreamSupport.stream(campaigns.spliterator(), false).map(CampaignMapper::toDTO).toList();
    }
}
