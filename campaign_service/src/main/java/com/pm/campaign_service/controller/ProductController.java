package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.ProductRequestDTO;
import com.pm.campaign_service.dto.ProductResponseDTO;
import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.service.ProductService;
import com.pm.campaign_service.util.UuidUtil;
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
@RequestMapping("/product")
@Tag(name = "Product", description = "API for managing Products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/get")
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponseDTO>> findAll(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getClaimAsString("id");
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(productService.findAll());
        } else {
            return ResponseEntity.ok(productService.findAllByUserId(userId));
        }
    }

    @PostMapping("/new")
    @Operation(summary = "Save new product")
    public ResponseEntity<ProductResponseDTO> save(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        ProductResponseDTO campaign = productService.saveWithUserId(productRequestDTO, userId);

        return ResponseEntity.ok(campaign);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update product with given id")
    public ResponseEntity<ProductResponseDTO> update(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ProductRequestDTO productRequestDTO, @PathVariable UUID id) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || productService.checkProductOwnership(userId, id)) {
            return ResponseEntity.ok(productService.update(id, productRequestDTO));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete product with given id")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable UUID id) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || productService.checkProductOwnership(userId, id)) {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get product with given id")
    public ResponseEntity<ProductResponseDTO> findById(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable UUID id) {

        UUID userId = UuidUtil.parseUuidOrThrow(jwt.getClaimAsString("id"));
        String role = jwt.getClaimAsString("role");

        if ("ADMIN".equals(role) || productService.checkProductOwnership(userId, id)) {
            return ResponseEntity.ok(productService.findById(id));
        } else {
            throw new CampaignOperationException("Campaign is not owned by the user");
        }
    }
}
