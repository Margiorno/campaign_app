package com.pm.campaign_service.mapper;

import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.model.Campaign;

public class CampaignMapper {
    public static CampaignResponseDTO toDTO(Campaign campaign) {
        CampaignResponseDTO campaignResponseDTO = new CampaignResponseDTO();
        campaignResponseDTO.setId(campaign.getId().toString());
        campaignResponseDTO.setName(campaign.getName());
        campaignResponseDTO.setDescription(campaign.getDescription());
        campaignResponseDTO.setProduct(campaign.getProduct().getId().toString());
        campaignResponseDTO.setKeywords(campaign.getKeywords());
        campaignResponseDTO.setBid_amount(campaign.getBid_amount());
        campaignResponseDTO.setCampaign_amount(campaign.getCampaign_amount());
        campaignResponseDTO.setActive(campaign.isActive());
        campaignResponseDTO.setCity(campaign.getCity());
        campaignResponseDTO.setRadius(campaign.getRadius());

        return campaignResponseDTO;
    }
}
