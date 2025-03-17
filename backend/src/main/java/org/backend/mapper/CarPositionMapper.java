package org.backend.mapper;

import org.backend.service.CarPositionService;
import org.dto.CarPositionDto;
import org.backend.model.CarPosition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarPositionMapper {

    public CarPositionDto toDto(CarPosition carPosition) {
        if (carPosition == null) {
            return null;
        }

        return CarPositionDto.builder()
                .id(carPosition.getId())
                .brand(carPosition.getBrand().getName())
                .model(carPosition.getModel().getName())
                .yearFrom(carPosition.getYearFrom())
                .yearBefore(carPosition.getYearBefore())
                .mileageFrom(carPosition.getMileageFrom())
                .mileageBefore(carPosition.getMileageBefore())
                .build();
    }
    public List<CarPositionDto> toDto(List<CarPosition> carPositions) {
        return carPositions == null ? Collections.emptyList() :
                carPositions.stream().map(this::toDto).collect(Collectors.toList());
    }
}

