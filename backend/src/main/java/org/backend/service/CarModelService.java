package org.backend.service;

import lombok.RequiredArgsConstructor;
import org.backend.mapper.CarModelMapper;
import org.dto.CarModelDto;
import org.backend.repository.CarModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;

    private final CarModelMapper carModelMapper;

    public List<CarModelDto> getCarModels() {
        return carModelMapper.toDto(carModelRepository.findAll());
    }

    public List<CarModelDto> getCarModelsByBrand(Long carBrandId) {
        return carModelMapper.toDto(carModelRepository.findCarModelByBrandId(carBrandId));
    }
}
