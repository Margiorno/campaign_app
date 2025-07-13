package com.pm.stats_service.controller;

import com.pm.stats_service.dto.StatsResponseDTO;
import com.pm.stats_service.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/all")
    private ResponseEntity<List<StatsResponseDTO>> findAll(){
        List<StatsResponseDTO> stats = statsService.findAll();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatsResponseDTO> findById(@PathVariable UUID id) {
        StatsResponseDTO stats = statsService.findById(id);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("{id}/click")
    public ResponseEntity<StatsResponseDTO> registerClick(@PathVariable UUID id) {
        StatsResponseDTO stats = statsService.registerClick(id);

        return ResponseEntity.ok(stats);
    }
}
