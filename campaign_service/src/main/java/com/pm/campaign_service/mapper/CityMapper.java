package com.pm.campaign_service.mapper;

import com.pm.campaign_service.dto.CityRequestDTO;
import com.pm.campaign_service.dto.CityResponseDTO;
import com.pm.campaign_service.model.City;

public class CityMapper {

    public static CityResponseDTO toDTO(City city) {
        CityResponseDTO cityResponseDTO = new CityResponseDTO();
        cityResponseDTO.setId(city.getId().toString());
        cityResponseDTO.setName(city.getName());
        cityResponseDTO.setLatitude(city.getLatitude());
        cityResponseDTO.setLongitude(city.getLongitude());

        return cityResponseDTO;
    }

    public static City toModel(CityRequestDTO cityRequestDTO) {
        City city = new City();
        city.setName(cityRequestDTO.getName());
        city.setLatitude(cityRequestDTO.getLatitude());
        city.setLongitude(cityRequestDTO.getLongitude());

        return city;
    }
}
