package com.pm.stats_service.service;

import com.pm.proto.CampaignProto;
import com.pm.stats_service.client.CampaignGrpcClient;
import com.pm.stats_service.dto.StatsResponseDTO;
import com.pm.stats_service.exception.StatsOperationException;
import com.pm.stats_service.mapper.StatsMapper;
import com.pm.stats_service.model.Stats;
import com.pm.stats_service.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        CampaignProto.CampaignResponse campaign;
        try {
            campaign = campaignGrpcClient.getCampaignById(id.toString());
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                throw new StatsOperationException("Campaign with that id does not exist: " + id);
            }
            throw new StatsOperationException(e.getMessage());
        }

        if (!campaign.getActive())
            throw new StatsOperationException("Campaign with that id is inactive");

        Stats stats = new Stats();
        stats.setId(UUID.randomUUID());

        return StatsMapper.toDTO(statsRepository.save(stats));
    }

    public void deleteById(UUID id) {
        if (!statsRepository.existsById(id)) {
            throw new StatsOperationException("Stats with that id does not exist: " + id);
        }

        try {
            campaignGrpcClient.deleteCampaign(id.toString());
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                throw new StatsOperationException("Campaign with that id does not exist in campaign service: " + id);
            }
            throw new StatsOperationException("Error while deleting campaign: " + e.getMessage());
        }

        statsRepository.deleteById(id);
    }


    public StatsResponseDTO registerClick(UUID id) {
        Stats stats = statsRepository.findById(id).orElseThrow(
                ()-> new StatsOperationException("Campaign with that id does not exists: " + id)
        );

        CampaignProto.CampaignResponse campaign;
        try {
            campaign = campaignGrpcClient.getCampaignById(id.toString());
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                throw new StatsOperationException("Campaign with that id does not exist: " + id);
            }
            throw new StatsOperationException(e.getMessage());
        }

        if (!campaign.getActive())
            throw new StatsOperationException("Campaign with that id is inactive");

        double campaignAmount = campaign.getCampaignAmount();
        double spentAmount = stats.getSpentAmount();
        double bidAmount = campaign.getBidAmount();

        if (campaignAmount - spentAmount < bidAmount){
            // TODO grpc endpoint to stop campaign

            throw new StatsOperationException("Not enough founds");
        }

        stats.setSpentAmount(stats.getSpentAmount() + campaignAmount);
        stats.setClicks(stats.getClicks() + 1);

        Stats saved = statsRepository.save(stats);
        return StatsMapper.toDTO(saved);

    }
}
