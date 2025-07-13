package com.pm.stats_service.service;

import com.pm.proto.CampaignProto;
import com.pm.stats_service.client.CampaignGrpcClient;
import com.pm.stats_service.dto.StatsResponseDTO;
import com.pm.stats_service.exception.StatsOperationException;
import com.pm.stats_service.mapper.StatsMapper;
import com.pm.stats_service.model.Stats;
import com.pm.stats_service.repository.StatsRepository;
import com.pm.stats_service.util.GrpcExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class StatsService {
    private final StatsRepository statsRepository;
    private final CampaignGrpcClient campaignGrpcClient;

    @Autowired
    public StatsService(StatsRepository statsRepository, CampaignGrpcClient campaignGrpcClient) {
        this.statsRepository = statsRepository;
        this.campaignGrpcClient = campaignGrpcClient;
    }

    public List<StatsResponseDTO> findAll() {
        Iterable<Stats> stats = statsRepository.findAll();

        return StreamSupport.stream(stats.spliterator(), false).map(StatsMapper::toDTO).toList();
    }

    public StatsResponseDTO findById(UUID id) {
        Stats stats = statsRepository.findById(id).orElseThrow(
                ()-> new StatsOperationException("Campaign with that id does not exists: " + id));

        return StatsMapper.toDTO(stats);
    }

    public StatsResponseDTO create(UUID id) {
        if (statsRepository.existsById(id))
            throw new StatsOperationException("Campaign with that id already exists: " + id);

        CampaignProto.CampaignResponse campaign = getCampaignOrThrow(id);
        validateCampaignIsActive(campaign);

        Stats stats = new Stats();
        stats.setId(id);

        return StatsMapper.toDTO(statsRepository.save(stats));
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!statsRepository.existsById(id)) {
            throw new StatsOperationException("Stats with that id does not exist: " + id);
        }

        statsRepository.deleteById(id);

        try {
            campaignGrpcClient.deleteCampaign(id.toString());
        } catch (io.grpc.StatusRuntimeException e) {
            throw GrpcExceptionUtil.mapToGrpcException(e, "Error while deleting campaign with id " + id);
        }
    }

    public StatsResponseDTO registerClick(UUID id) {
        Stats stats = statsRepository.findById(id).orElseThrow(
                ()-> new StatsOperationException("Campaign with that id does not exists: " + id)
        );

        CampaignProto.CampaignResponse campaign = getCampaignOrThrow(id);
        validateCampaignIsActive(campaign);

        double campaignAmount = campaign.getCampaignAmount();
        double spentAmount = stats.getSpentAmount();
        double bidAmount = campaign.getBidAmount();

        if (campaignAmount - spentAmount < bidAmount){
            try {
                campaignGrpcClient.stopCampaign(id.toString());
            } catch (io.grpc.StatusRuntimeException e) {
                throw GrpcExceptionUtil.mapToGrpcException(e, "Failed to stop campaign with id " + id);
            }

            throw new StatsOperationException("Not enough funds");
        }

        stats.setSpentAmount(stats.getSpentAmount() + bidAmount);
        stats.setClicks(stats.getClicks() + 1);

        Stats saved = statsRepository.save(stats);
        return StatsMapper.toDTO(saved);
    }



    // UTIL METHODS:
    private CampaignProto.CampaignResponse getCampaignOrThrow(UUID id) {
        try {
            return campaignGrpcClient.getCampaignById(id.toString());
        } catch (io.grpc.StatusRuntimeException e) {
            throw GrpcExceptionUtil.mapToGrpcException(e, "Failed to fetch campaign with id " + id);
        }
    }

    private void validateCampaignIsActive(CampaignProto.CampaignResponse campaign) {
        if (!campaign.getActive()) {
            throw new StatsOperationException("Campaign with that id is inactive");
        }
    }
}
