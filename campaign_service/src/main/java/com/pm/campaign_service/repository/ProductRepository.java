package com.pm.campaign_service.repository;

import com.pm.campaign_service.model.City;
import com.pm.campaign_service.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
}
