package com.pm.campaign_service.mapper;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.model.City;
import com.pm.campaign_service.model.Product;

import java.util.ArrayList;
import java.util.List;

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
        campaignResponseDTO.setCity(campaign.getCity().getId().toString());
        campaignResponseDTO.setRadius(campaign.getRadius());

        return campaignResponseDTO;
    }

    public static Campaign toModel(CampaignRequestDTO campaignRequestDTO, Product product, City city) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignRequestDTO.getName());
        if (campaignRequestDTO.getDescription() != null) {
            campaign.setDescription(campaignRequestDTO.getDescription());
        }
        campaign.setProduct(product);
        campaign.setKeywords(
                campaignRequestDTO.getKeywords().isEmpty() || campaignRequestDTO.getKeywords() == null?
                        new ArrayList<>(List.of(campaignRequestDTO.getName())) : campaignRequestDTO.getKeywords()
        );
        campaign.setBid_amount(campaignRequestDTO.getBid_amount());
        campaign.setCampaign_amount(campaignRequestDTO.getCampaign_amount());
        campaign.setActive(false);
        campaign.setCity(city);
        campaign.setRadius(campaignRequestDTO.getRadius());

        return campaign;
    }
}
