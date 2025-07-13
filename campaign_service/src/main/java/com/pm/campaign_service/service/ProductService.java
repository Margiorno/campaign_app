package com.pm.campaign_service.service;

import com.pm.campaign_service.dto.CampaignResponseDTO;
import com.pm.campaign_service.dto.ProductRequestDTO;
import com.pm.campaign_service.dto.ProductResponseDTO;
import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.exception.ProductOperationException;
import com.pm.campaign_service.mapper.CampaignMapper;
import com.pm.campaign_service.mapper.ProductMapper;
import com.pm.campaign_service.model.Campaign;
import com.pm.campaign_service.model.CampaignOwnership;
import com.pm.campaign_service.model.Product;
import com.pm.campaign_service.model.ProductOwnership;
import com.pm.campaign_service.repository.ProductOwnershipRepository;
import com.pm.campaign_service.repository.ProductRepository;
import com.pm.campaign_service.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOwnershipRepository productOwnershipRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductOwnershipRepository productOwnershipRepository) {
        this.productRepository = productRepository;
        this.productOwnershipRepository = productOwnershipRepository;
    }

    public boolean existsById(UUID uuid) {
        return productRepository.existsById(uuid);
    }

    public List<ProductResponseDTO> findAll() {
        Iterable<Product> products = productRepository.findAll();

        return StreamSupport.stream(products.spliterator(), false).map(ProductMapper::toDTO).toList();
    }

    public List<ProductResponseDTO> findAllByUserId(String userId) {
        UUID uuid = UuidUtil.parseUuidOrThrow(userId);

        Iterable<ProductOwnership> campaigns = productOwnershipRepository.findByUserId(uuid);

        return StreamSupport.stream(campaigns.spliterator(),false)
                .map(ProductOwnership::getProduct)
                .map(ProductMapper::toDTO).toList();
    }

    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {
        if (productRepository.existsByName(productRequestDTO.getName()))
            throw new ProductOperationException("Product with name " + productRequestDTO.getName() + " already exists");

        Product product = ProductMapper.toModel(productRequestDTO);
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductResponseDTO saveWithUserId(ProductRequestDTO productRequestDTO, UUID userId) {
        ProductResponseDTO product = save(productRequestDTO);
        assignCampaignToUser(UuidUtil.parseUuidOrThrow(product.getId()), userId);

        return product;
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

    @Transactional
    protected void assignCampaignToUser(UUID productId, UUID userId) {
        ProductOwnership ownership = new ProductOwnership();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CampaignOperationException("Product with this id does not exist: " + productId));

        ownership.setUserId(userId);
        ownership.setProduct(product);

        productOwnershipRepository.save(ownership);
    }

    public boolean checkProductOwnership(UUID userId, UUID id) {
        return productOwnershipRepository.existsByProduct_IdAndUserId(id, userId);
    }
}
