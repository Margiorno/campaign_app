package com.pm.campaign_service.mapper;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.model.City;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.service.CampaignService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

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



    //    @NotNull
    //    private LocalDateTime created_at;
    //
    //    @NotNull
    //    private LocalDateTime updated_at;


    @NotEmpty
    private double bid_amount;

    private double campaign_amount;

    @NotBlank
    private String city;

    private int radius;



    public static Campaign toModel(CampaignRequestDTO campaignRequestDTO, Product product, City city) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignRequestDTO.getName());
        if (campaignRequestDTO.getDescription() != null) {
            campaign.setDescription(campaignRequestDTO.getDescription());
        }
        campaign.setProduct(product);
        campaign.setKeywords(campaignRequestDTO.getKeywords());
        campaign.setBid_amount(campaignRequestDTO.getBid_amount());
        campaign.setCampaign_amount(campaignRequestDTO.getCampaign_amount());
        campaign.setActive(false);
        campaign.setCity(city);
        campaign.setRadius(campaignRequestDTO.getRadius());

        return campaign;
    }
}
