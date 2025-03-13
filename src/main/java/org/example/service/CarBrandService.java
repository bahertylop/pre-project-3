package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarBrandDto;
import org.example.repository.CarBrandRepository;
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
