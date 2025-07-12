package com.pm.campaign_service.controller;

import com.pm.campaign_service.dto.ProductRequestDTO;
import com.pm.campaign_service.dto.ProductResponseDTO;
import com.pm.campaign_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAll();

        return ResponseEntity.ok(products);
    }

    @PostMapping("/add")
    @Operation(summary = "Save new product")
    public ResponseEntity<ProductResponseDTO> save(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO product = productService.save(productRequestDTO);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update product with given id")
    public ResponseEntity<ProductResponseDTO> update(@Valid @RequestBody ProductRequestDTO productRequestDTO, @PathVariable UUID id) {
        ProductResponseDTO product = productService.update(id, productRequestDTO);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete product with given id")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get product with given id")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        ProductResponseDTO city = productService.findById(id);

        return ResponseEntity.ok(city);
    }
}
