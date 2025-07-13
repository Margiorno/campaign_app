package com.pm.campaign_service.service;

import com.pm.campaign_service.dto.CityRequestDTO;
import com.pm.campaign_service.dto.CityResponseDTO;
import com.pm.campaign_service.exception.CityOperationException;
import com.pm.campaign_service.mapper.CityMapper;
import com.pm.campaign_service.model.City;
import com.pm.campaign_service.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityResponseDTO> findAll() {

        Iterable<City> cities = cityRepository.findAll();

        return StreamSupport.stream(cities.spliterator(), false).map(CityMapper::toDTO).toList();
    }

    public CityResponseDTO save(CityRequestDTO cityRequestDTO) {
        if (cityRepository.existsByNameIgnoreCase(cityRequestDTO.getName()))
            throw new CityOperationException("City with this name already exists: " + cityRequestDTO.getName());

        City city = CityMapper.toModel(cityRequestDTO);
        City savedCity = cityRepository.save(city);

        return CityMapper.toDTO(savedCity);
    }

    public CityResponseDTO update(UUID id, CityRequestDTO cityRequestDTO) {
        if (!cityRepository.existsById(id))
            throw new CityOperationException("City with this id does not exist: " + cityRequestDTO.getName());

        City city = CityMapper.toModel(cityRequestDTO);
        city.setId(id);

        City savedCity = cityRepository.save(city);

        return CityMapper.toDTO(savedCity);
    }

    public void delete(UUID id) {
        if (!cityRepository.existsById(id))
            throw new CityOperationException("City with this id does not exist: " + id);

        cityRepository.deleteById(id);
    }

    public CityResponseDTO findById(UUID id) {
        return CityMapper.toDTO(cityRepository.findById(id)
                .orElseThrow(() -> new CityOperationException("City with this id does not exist: " + id)));
    }

    public City findById(String id) {
        return cityRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new CityOperationException("City with this id does not exist: " + id));
    }

    public boolean existsById(UUID id) {
        return cityRepository.existsById(id);
    }
}
