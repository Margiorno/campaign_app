package com.pm.campaign_service.service;

import com.pm.campaign_service.client.StatsGrpcClient;
import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.validator.CreateCampaignValidationGroup;
import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.mapper.CampaignMapper;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.model.City;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.repository.CampaignRepository;
import com.pm.campaign_service.util.GrpcExceptionUtil;
import com.pm.campaign_service.util.UuidUtil;
import io.grpc.StatusRuntimeException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CityService cityService;
    private final ProductService productService;
    private final StatsGrpcClient statsGrpcClient;
    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, CityService cityService, ProductService productService, StatsGrpcClient statsGrpcClient) {
        this.campaignRepository = campaignRepository;
        this.cityService = cityService;
        this.productService = productService;
        this.statsGrpcClient = statsGrpcClient;
    }

    public List<CampaignResponseDTO> findAll() {
        Iterable<Campaign> campaigns = campaignRepository.findAll();

        return StreamSupport.stream(campaigns.spliterator(), false).map(CampaignMapper::toDTO).toList();
    }

    public CampaignResponseDTO findById(UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignOperationException("Campaign with this id does not exist: " + id));

        return CampaignMapper.toDTO(campaign);
    }

    @Transactional
    public CampaignResponseDTO save(CampaignRequestDTO campaignRequestDTO) {

        if (!cityService.existsById(UuidUtil.parseUuidOrThrow(campaignRequestDTO.getCity())))
            throw new CampaignOperationException("City with this id does not exist: " + campaignRequestDTO.getCity());

        if(!productService.existsById(UuidUtil.parseUuidOrThrow(campaignRequestDTO.getProduct())))
            throw new CampaignOperationException("Product with this id does not exist: " + campaignRequestDTO.getProduct());

        if(campaignRepository.existsByName(campaignRequestDTO.getName()))
            throw new CampaignOperationException("Campaign with this name already exists: " + campaignRequestDTO.getName());

        City city = cityService.findById(campaignRequestDTO.getCity());
        Product product = productService.findById(campaignRequestDTO.getProduct());

        Campaign campaign = CampaignMapper.toModel(campaignRequestDTO,product,city);
        campaign.setActive(true);
        campaign.setCreated_at(LocalDateTime.now());
        campaign.setUpdated_at(LocalDateTime.now());

        Campaign savedCampaign = campaignRepository.save(campaign);

        try {
            statsGrpcClient.createStats(savedCampaign.getId().toString());
        } catch (StatusRuntimeException e) {
            throw GrpcExceptionUtil.mapToGrpcException(e, "Failed to create stats for campaign");
        }

        return CampaignMapper.toDTO(savedCampaign);
    }

    @Transactional
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

        if (campaignRequestDTO.getBid_amount() != null)
            campaign.setBid_amount(Double.parseDouble(campaignRequestDTO.getBid_amount()));

        if (campaignRequestDTO.getCampaign_amount() != null) {
            campaign.setCampaign_amount(Double.parseDouble(campaignRequestDTO.getCampaign_amount()));

            if (Double.parseDouble(campaignRequestDTO.getCampaign_amount()) < campaign.getBid_amount())
                campaign.setActive(false);
        }

        if (campaignRequestDTO.getCity() != null)
            campaign.setCity(cityService.findById(campaignRequestDTO.getCity()));

        if (campaignRequestDTO.getRadius() != null)
            campaign.setRadius(Double.parseDouble(campaignRequestDTO.getRadius()));

        campaign.setUpdated_at(LocalDateTime.now());

        double spentAmount;
        try {
            spentAmount = statsGrpcClient.getStatsById(id.toString()).getSpentAmount();
        } catch (StatusRuntimeException e) {
            throw GrpcExceptionUtil.mapToGrpcException(e, "Failed to get stats for campaign: " + id);
        }

        if (spentAmount + campaign.getBid_amount() > campaign.getCampaign_amount())
            campaign.setActive(false);

        Campaign savedCampaign = campaignRepository.save(campaign);

        return CampaignMapper.toDTO(savedCampaign);
    }

    @Transactional
    public void delete(UUID id) {

        if (!campaignRepository.existsById(id)) {
            throw new CampaignOperationException("Campaign with this id does not exist: " + id);
        }

        try {
            statsGrpcClient.deleteStats(id.toString());
        } catch (StatusRuntimeException e) {
            logger.error("Status: {}, Message: {}", e.getStatus(), e.getMessage(), e);
            throw GrpcExceptionUtil.mapToGrpcException(e, "Failed to get stats for campaign: " + id);
        }

        campaignRepository.deleteById(id);
    }

    @Transactional
    public CampaignResponseDTO start(UUID id) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new CampaignOperationException("Campaign with this id does not exist: " + id));

        if (campaign.getBid_amount() > campaign.getCampaign_amount())
            throw new CampaignOperationException("Not enough founds" + campaign.getCampaign_amount());

        campaign.setActive(true);
        campaign.setUpdated_at(LocalDateTime.now());

        return CampaignMapper.toDTO(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponseDTO stop(UUID id) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new CampaignOperationException("Campaign with this id does not exist: " + id));
        campaign.setActive(false);
        campaign.setUpdated_at(LocalDateTime.now());

        return CampaignMapper.toDTO(campaignRepository.save(campaign));
    }
}
