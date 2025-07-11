package com.pm.campaign_service.repository;

import com.pm.campaign_service.model.City;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CityRepository extends CrudRepository<City, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
