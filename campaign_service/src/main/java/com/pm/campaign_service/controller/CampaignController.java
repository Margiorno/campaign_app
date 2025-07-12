package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.validator.CreateCampaignValidationGroup;
import com.pm.campaign_service.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all campaigns")
    public ResponseEntity<List<CampaignResponseDTO>> findAll() {
        List<CampaignResponseDTO> campaigns = campaignService.findAll();

        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get campaign with given id")
    public ResponseEntity<CampaignResponseDTO> findById(@PathVariable UUID id) {
        CampaignResponseDTO campaign = campaignService.findById(id);

        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/new")
    @Operation(summary = "Save new campaign")
    public ResponseEntity<CampaignResponseDTO> save(
            @Validated({Default.class, CreateCampaignValidationGroup.class}) @RequestBody CampaignRequestDTO campaignRequestDTO) {
        CampaignResponseDTO campaign = campaignService.save(campaignRequestDTO);

        return ResponseEntity.ok(campaign);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update campaign with given id")
    public ResponseEntity<CampaignResponseDTO> update(
            @PathVariable UUID id, @Validated({Default.class}) @RequestBody CampaignRequestDTO campaignRequestDTO) {
        CampaignResponseDTO campaignResponseDTO = campaignService.update(campaignRequestDTO, id);

        return ResponseEntity.ok(campaignResponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete campaign with given id")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        campaignService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start campaign with given id")
    public ResponseEntity<CampaignResponseDTO> start(@PathVariable UUID id) {
        CampaignResponseDTO campaign = campaignService.start(id);

        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/{id}/stop")
    @Operation(summary = "Stop campaign with given id")
    public ResponseEntity<CampaignResponseDTO> stop(@PathVariable UUID id) {
        CampaignResponseDTO campaign = campaignService.stop(id);

        return ResponseEntity.ok(campaign);
    }
}
