package com.pm.stats_service.repository;

import com.pm.stats_service.model.Stats;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StatsRepository extends CrudRepository<Stats, UUID> { }
