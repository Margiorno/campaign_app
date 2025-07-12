package com.pm.stats_service.service;

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

    @Autowired
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
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

        // TODO communication with campaign service
        // TODO check if active must be done

        Stats stats = new Stats();
        stats.setId(UUID.randomUUID());

        return StatsMapper.toDTO(statsRepository.save(stats));
    }

    public void deleteById(UUID id) {
        if (!statsRepository.existsById(id))
            throw new StatsOperationException("Campaign with that id already exists: " + id);

        // TODO communication with campaign service
        // delete or make inactive?

        statsRepository.deleteById(id);
    }

    public StatsResponseDTO registerClick(UUID id) {
        Stats stats = statsRepository.findById(id).orElseThrow(
                ()-> new StatsOperationException("Campaign with that id does not exists: " + id)
        );

        // TODO communication with campaign service
        // does we still have founds?

        Stats saved = statsRepository.save(stats);
        return StatsMapper.toDTO(saved);

    }
}
