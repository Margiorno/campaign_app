package com.pm.campaign_service.service;

import com.pm.campaign_service.exception.ProductOperationException;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public boolean existsById(UUID uuid) {
        return productRepository.existsById(uuid);
    }

    public Product findById(String id) {
        return productRepository.findById(UUID.fromString(id))
                .orElseThrow(()->new ProductOperationException("Product with id " + id + " not found"));
    }
}
