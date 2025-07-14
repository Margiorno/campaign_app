package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.validator.CreateCampaignValidationGroup;
import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.service.CampaignService;
import com.pm.campaign_service.util.UuidUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/campaign")
@Tag(name = "Campaign", description = "API for managing Campaigns")
public class CampaignController {
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all campaigns")
    public ResponseEntity<List<CampaignResponseDTO>> findAll(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("id");
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(campaignService.findAll());
        } else {
            return ResponseEntity.ok(campaignService.findAllByUserId(userId));
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Get all campaigns")
    public ResponseEntity<List<CampaignResponseDTO>> findActive() {

        return ResponseEntity.ok(campaignService.findAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get campaign with given id")
    public ResponseEntity<CampaignResponseDTO> findById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || campaignService.checkCampaignOwnership(userId, id)) {
            return ResponseEntity.ok(campaignService.findById(id));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }

    @PostMapping("/new")
    @Operation(summary = "Save new campaign")
    public ResponseEntity<CampaignResponseDTO> save(
            @AuthenticationPrincipal Jwt jwt,
            @Validated({Default.class, CreateCampaignValidationGroup.class}) @RequestBody CampaignRequestDTO campaignRequestDTO) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        CampaignResponseDTO campaign = campaignService.saveWithUserId(campaignRequestDTO, userId);

        return ResponseEntity.ok(campaign);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update campaign with given id")
    public ResponseEntity<CampaignResponseDTO> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id, @Validated({Default.class}) @RequestBody CampaignRequestDTO campaignRequestDTO) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || campaignService.checkCampaignOwnership(userId, id)) {
            return ResponseEntity.ok(campaignService.update(campaignRequestDTO, id));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete campaign with given id")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || campaignService.checkCampaignOwnership(userId, id)) {
            campaignService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }

    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start campaign with given id")
    public ResponseEntity<CampaignResponseDTO> start(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || campaignService.checkCampaignOwnership(userId, id)) {
            return ResponseEntity.ok(campaignService.start(id));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }

    @PostMapping("/{id}/stop")
    @Operation(summary = "Stop campaign with given id")
    public ResponseEntity<CampaignResponseDTO> stop(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || campaignService.checkCampaignOwnership(userId, id)) {
            return ResponseEntity.ok(campaignService.stop(id));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }
}
