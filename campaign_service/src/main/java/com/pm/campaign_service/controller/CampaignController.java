package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    private CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/all")
    public List<CampaignResponseDTO> findAll() {
        List<CampaignResponseDTO> campaigns = campaignService.findAll();

        return campaigns;
    }


}
