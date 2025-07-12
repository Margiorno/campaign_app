package com.pm.campaign_service.mapper;

import com.pm.campaign_service.dto.ProductRequestDTO;
import com.pm.campaign_service.dto.ProductResponseDTO;
import com.pm.campaign_service.model.Product;

public class ProductMapper {

    public static ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId().toString());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());

        return productResponseDTO;
    }

    public static Product toModel(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());

        return product;
    }
}
