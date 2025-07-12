package com.pm.campaign_service.service;

import com.pm.campaign_service.dto.ProductRequestDTO;
import com.pm.campaign_service.dto.ProductResponseDTO;
import com.pm.campaign_service.exception.ProductOperationException;
import com.pm.campaign_service.mapper.ProductMapper;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean existsById(UUID uuid) {
        return productRepository.existsById(uuid);
    }

    public List<ProductResponseDTO> findAll() {
        Iterable<Product> products = productRepository.findAll();

        return StreamSupport.stream(products.spliterator(), false).map(ProductMapper::toDTO).toList();
    }

    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {
        if (productRepository.existsByName(productRequestDTO.getName()))
            throw new ProductOperationException("Product with name " + productRequestDTO.getName() + " already exists");

        Product product = ProductMapper.toModel(productRequestDTO);
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDTO(savedProduct);
    }

    public ProductResponseDTO update(UUID id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductOperationException("Product with id " + id + " not found"));

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    public void delete(UUID id) {
        if (!productRepository.existsById(id))
            throw new ProductOperationException("Product with id " + id + " not found");

        productRepository.deleteById(id);
    }

    public ProductResponseDTO findById(UUID id) {
        return ProductMapper.toDTO(
                productRepository.findById(id)
                        .orElseThrow(()->new ProductOperationException("Product with id " + id + " not found")));
    }

    public Product findById(String id) {
        return productRepository.findById(UUID.fromString(id))
                .orElseThrow(()->new ProductOperationException("Product with id " + id + " not found"));
    }
}
