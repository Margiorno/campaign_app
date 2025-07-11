package com.pm.campaign_service.service;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.validator.CreateCampaignValidationGroup;
import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.exception.CityOperationException;
import com.pm.campaign_service.mapper.CampaignMapper;
import com.pm.campaign_service.mapper.CityMapper;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.model.City;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.repository.CampaignRepository;
import com.pm.campaign_service.repository.CityRepository;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;

    private final CityService cityService;
    private final ProductService productService;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, CityService cityService, ProductService productService) {
        this.campaignRepository = campaignRepository;
        this.cityService = cityService;
        this.productService = productService;
    }

    public List<CampaignResponseDTO> findAll() {
        Iterable<Campaign> campaigns = campaignRepository.findAll();

        return StreamSupport.stream(campaigns.spliterator(), false).map(CampaignMapper::toDTO).toList();
    }

    public CampaignResponseDTO save(CampaignRequestDTO campaignRequestDTO) {
        if (!cityService.existsById(UUID.fromString(campaignRequestDTO.getCity())))
            throw new CampaignOperationException("City with this id does not exist: " + campaignRequestDTO.getCity());

        if(productService.existsById(UUID.fromString(campaignRequestDTO.getProduct())))
            throw new CampaignOperationException("Product with this id does not exist: " + campaignRequestDTO.getProduct());

        if(campaignRepository.existsByName(campaignRequestDTO.getName()))
            throw new CampaignOperationException("Campaign with this name already exists: " + campaignRequestDTO.getName());

        City city = cityService.findById(campaignRequestDTO.getCity());
        Product product = productService.findById(campaignRequestDTO.getProduct());

        Campaign campaign = CampaignMapper.toModel(campaignRequestDTO,product,city);
        campaign.setCreated_at(LocalDateTime.now());
        campaign.setUpdated_at(LocalDateTime.now());

        Campaign savedCampaign = campaignRepository.save(campaign);

        return CampaignMapper.toDTO(savedCampaign);
    }


    @NotBlank(groups = CreateCampaignValidationGroup.class, message = "City is required")
    private String city;

    private int radius;

    public CampaignResponseDTO update(CampaignRequestDTO campaignRequestDTO, UUID id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignOperationException("Campaign with this id does not exist: " + id));

        if (campaignRequestDTO.getName() != null)
            campaign.setName(campaignRequestDTO.getName());

        if (campaignRequestDTO.getDescription() != null)
            campaign.setDescription(campaignRequestDTO.getDescription());

        if (campaignRequestDTO.getProduct() != null)
            campaign.setProduct(productService.findById(campaignRequestDTO.getProduct()));

        if (campaignRequestDTO.getKeywords() != null && !campaignRequestDTO.getKeywords().isEmpty())
            campaign.setKeywords(campaignRequestDTO.getKeywords());

        if (campaignRequestDTO.getBid_amount() != 0.0)
            campaign.setBid_amount(campaignRequestDTO.getBid_amount());
        // TODO I have to find solution to problem that bid amount is null

        if (campaignRequestDTO.getCampaign_amount() != 0.0) {
            campaign.setCampaign_amount(campaignRequestDTO.getCampaign_amount());

            // TODO CAMPAIGN_AMOUNT < BID_AMOUNT && REBUILD THIS && I have to find solution to problem that campaign amount is null
        }

        if (campaignRequestDTO.getCity() != null)
            campaign.setCity(cityService.findById(campaignRequestDTO.getCity()));

        if (campaignRequestDTO.getRadius() != 0)
            campaign.setRadius(campaignRequestDTO.getRadius());
        // TODO I have to find solution to problem that radius is null

        // NOTE: maybe string in request instead of double/int is sollution to those problems

        campaign.setUpdated_at(LocalDateTime.now());

        Campaign savedCampaign = campaignRepository.save(campaign);
        return CampaignMapper.toDTO(savedCampaign);
    }
}
