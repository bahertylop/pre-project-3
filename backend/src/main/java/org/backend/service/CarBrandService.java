package org.backend.service;

import lombok.RequiredArgsConstructor;
import org.backend.mapper.CarBrandMapper;
import org.dto.CarBrandDto;
import org.backend.repository.CarBrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;

    private final CarBrandMapper carBrandMapper;

    public List<CarBrandDto> getCarBrands() {
        return carBrandMapper.toDto(carBrandRepository.findAll());
    }
}
