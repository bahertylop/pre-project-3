package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarModelDto;
import org.example.repository.CarModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;

    public List<CarModelDto> getCarModels() {
        return CarModelDto.from(carModelRepository.findAll());
    }

    public List<CarModelDto> getCarModelsByBrand(Long carBrandId) {
        return CarModelDto.from(carModelRepository.findCarModelByBrandId(carBrandId));
    }
}
