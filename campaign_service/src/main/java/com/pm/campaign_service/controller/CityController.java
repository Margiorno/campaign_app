package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CityRequestDTO;
import com.pm.campaign_service.dto.CityResponseDTO;
import com.pm.campaign_service.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/city")
public class CityController {
    private CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<CityResponseDTO>> findAll() {
        List<CityResponseDTO> cities = cityService.findAll();

        return ResponseEntity.ok(cities);
    }

    @PostMapping("/add")
    public ResponseEntity<CityResponseDTO> save(@Valid @RequestBody CityRequestDTO cityRequestDTO) {
        CityResponseDTO city = cityService.save(cityRequestDTO);

        return ResponseEntity.ok(city);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CityResponseDTO> update(@Valid @RequestBody CityRequestDTO cityRequestDTO, @PathVariable UUID id) {
        CityResponseDTO city = cityService.update(id, cityRequestDTO);

        return ResponseEntity.ok(city);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cityService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CityResponseDTO> findById(@PathVariable UUID id) {
        CityResponseDTO city = cityService.findById(id);

        return ResponseEntity.ok(city);
    }
}
