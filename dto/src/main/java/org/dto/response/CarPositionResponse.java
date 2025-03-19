package org.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.dto.CarPositionPriceDto;
import org.backend.model.CarPosition;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarPositionResponse {

    private Long id;

    private String brand;

    private String model;

    private Integer yearFrom;

    private Integer yearBefore;

    private Integer mileageFrom;

    private Integer mileageBefore;

    private List<CarPositionPriceDto> prices;

    public static CarPositionResponse from(CarPosition carPosition) {
        return CarPositionResponse.builder()
                .id(carPosition.getId())
                .brand(carPosition.getBrand().getName())
                .model(carPosition.getModel().getName())
                .yearFrom(carPosition.getYearFrom())
                .yearBefore(carPosition.getYearBefore())
                .mileageFrom(carPosition.getMileageFrom())
                .mileageBefore(carPosition.getMileageBefore())
                .prices(CarPositionPriceDto.from(carPosition.getPrices()).stream().sorted(Comparator.comparing(CarPositionPriceDto::getDate)).collect(Collectors.toList()))
                .build();
    }
}
