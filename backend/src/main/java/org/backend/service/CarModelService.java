package org.backend.service;

import lombok.RequiredArgsConstructor;
import org.backend.dto.CarModelDto;
import org.backend.repository.CarModelRepository;
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
