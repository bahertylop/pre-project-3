package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarPositionDto {

    private Long id;

    private String brand;

    private String model;

    private Integer yearFrom;

    private Integer yearBefore;

    private Integer mileageFrom;

    private Integer mileageBefore;

    public static CarPositionDto from(CarPosition carPosition) {
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

    public static List<CarPositionDto> from(List<CarPosition> carPositions) {
        return carPositions.stream().map(CarPositionDto::from).collect(Collectors.toList());
    }
}
