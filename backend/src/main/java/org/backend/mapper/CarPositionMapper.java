package org.backend.mapper;

import lombok.RequiredArgsConstructor;
import org.backend.service.CarPositionService;
import org.dto.CarPositionDto;
import org.backend.model.CarPosition;
import org.dto.CarPositionPriceDto;
import org.dto.response.CarPositionResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarPositionMapper {

    private final CarPositionPriceMapper pricesMapper;

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

    public CarPositionResponse toCarPositionResponse(CarPosition carPosition) {
        return CarPositionResponse.builder()
                .id(carPosition.getId())
                .brand(carPosition.getBrand().getName())
                .model(carPosition.getModel().getName())
                .yearFrom(carPosition.getYearFrom())
                .yearBefore(carPosition.getYearBefore())
                .mileageFrom(carPosition.getMileageFrom())
                .mileageBefore(carPosition.getMileageBefore())
                .prices(pricesMapper.toDto(carPosition.getPrices()).stream()
                        .sorted(Comparator.comparing(CarPositionPriceDto::getDate))
                        .collect(Collectors.toList()))
                .build();
    }
}

