package org.backend.mapper;


import org.backend.model.CarBrand;
import org.dto.CarBrandDto;
import org.dto.CarPositionDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarBrandMapper {

    public CarBrandDto toDto(CarBrand carBrand) {
        return CarBrandDto.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .value(carBrand.getValue())
                .build();
    }

    public List<CarBrandDto> toDto(List<CarBrand> carBrands) {
        return carBrands.stream().map(this::toDto).collect(Collectors.toList());
    }
}
