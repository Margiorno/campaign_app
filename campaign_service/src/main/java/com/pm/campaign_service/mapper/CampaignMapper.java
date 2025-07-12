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
        campaignResponseDTO.setBid_amount(Double.toString(campaign.getBid_amount()));
        campaignResponseDTO.setCampaign_amount(Double.toString(campaign.getCampaign_amount()));
        campaignResponseDTO.setActive(String.valueOf(campaign.isActive()));
        campaignResponseDTO.setCity(campaign.getCity().getId().toString());
        campaignResponseDTO.setRadius(Double.toString(campaign.getRadius()));

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
                campaignRequestDTO.getKeywords() == null ||campaignRequestDTO.getKeywords().isEmpty() ?
                        new ArrayList<>(List.of(campaignRequestDTO.getName())) : campaignRequestDTO.getKeywords()
        );
        campaign.setBid_amount(Double.parseDouble(campaignRequestDTO.getBid_amount()));
        campaign.setCampaign_amount(Double.parseDouble(campaignRequestDTO.getCampaign_amount()));
        campaign.setCity(city);
        campaign.setRadius(Double.parseDouble(campaignRequestDTO.getRadius()));

        return campaign;
    }
}
