package com.pm.stats_service.mapper;

import com.pm.stats_service.dto.StatsResponseDTO;
import com.pm.stats_service.model.Stats;

public class StatsMapper {
    public static StatsResponseDTO toDTO(Stats stats) {
        StatsResponseDTO dto = new StatsResponseDTO();

        dto.setId(stats.getCampaignId().toString());
        dto.setClicks(stats.getClicks());
        dto.setSpentAmount(stats.getSpentAmount());

        return dto;
    }
}
