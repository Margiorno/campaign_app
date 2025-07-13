package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CityRequestDTO;
import com.pm.campaign_service.dto.CityResponseDTO;
import com.pm.campaign_service.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/city")
@Tag(name = "Product", description = "API for managing Cities")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/get")
    @Operation(summary = "Get all cities")
    public ResponseEntity<List<CityResponseDTO>> findAll() {
        List<CityResponseDTO> cities = cityService.findAll();

        return ResponseEntity.ok(cities);
    }

    @PostMapping("/add")
    @Operation(summary = "Save new city")
    public ResponseEntity<CityResponseDTO> save(
            @Valid @RequestBody CityRequestDTO cityRequestDTO) {
        CityResponseDTO city = cityService.save(cityRequestDTO);

        return ResponseEntity.ok(city);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update city with given id")
    public ResponseEntity<CityResponseDTO> update(@Valid @RequestBody CityRequestDTO cityRequestDTO, @PathVariable UUID id) {
        CityResponseDTO city = cityService.update(id, cityRequestDTO);

        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete city with given id")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cityService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get city with given id")
    public ResponseEntity<CityResponseDTO> findById(@PathVariable UUID id) {
        CityResponseDTO city = cityService.findById(id);

        return ResponseEntity.ok(city);
    }
}
