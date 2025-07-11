package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CampaignResponseDTO>> findAll() {
        List<CampaignResponseDTO> campaigns = campaignService.findAll();

        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("/new")
    public ResponseEntity<CampaignResponseDTO> save(@Valid @RequestBody CampaignRequestDTO campaignRequestDTO) {
        CampaignResponseDTO campaign = campaignService.save(campaignRequestDTO);

        return ResponseEntity.ok(campaign);
    }
}
