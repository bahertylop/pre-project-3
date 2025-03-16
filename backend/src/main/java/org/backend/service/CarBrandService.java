package org.backend.service;

import lombok.RequiredArgsConstructor;
import org.backend.dto.CarBrandDto;
import org.backend.repository.CarBrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;

    public List<CarBrandDto> getCarBrands() {
        return CarBrandDto.from(carBrandRepository.findAll());
    }
}
