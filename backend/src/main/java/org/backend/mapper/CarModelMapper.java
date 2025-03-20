package org.backend.mapper;

import org.backend.model.CarModel;
import org.dto.CarModelDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarModelMapper {

    public CarModelDto toDto(CarModel carModel) {
        return CarModelDto.builder()
                .name(carModel.getName())
                .value(carModel.getValue())
                .build();
    }

    public List<CarModelDto> toDto(List<CarModel> carModels) {
        return carModels.stream().map(this::toDto).collect(Collectors.toList());
    }
}
