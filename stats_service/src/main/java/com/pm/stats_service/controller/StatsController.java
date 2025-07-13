package com.pm.stats_service.controller;

import com.pm.stats_service.dto.StatsResponseDTO;
import com.pm.stats_service.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stats")
@Tag(name = "Stats", description = "API for managing stats")
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all stats")
    private ResponseEntity<List<StatsResponseDTO>> findAll(){
        List<StatsResponseDTO> stats = statsService.findAll();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stats with given id")
    public ResponseEntity<StatsResponseDTO> findById(@PathVariable UUID id) {
        StatsResponseDTO stats = statsService.findById(id);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("{id}/click")
    @Operation(summary = "Click stats with given id")
    public ResponseEntity<StatsResponseDTO> registerClick(@PathVariable UUID id) {
        StatsResponseDTO stats = statsService.registerClick(id);

        return ResponseEntity.ok(stats);
    }
}
