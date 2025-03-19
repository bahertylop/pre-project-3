package org.backend.mapper;

import org.dto.CarPositionDto;
import org.dto.CarPositionPriceDto;
import org.backend.model.PositionPrice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarPositionPriceMapper {

    public CarPositionPriceDto toDto(PositionPrice positionPrice) {
        return CarPositionPriceDto.builder()
                .price(positionPrice.getPrice())
                .date(positionPrice.getDate())
                .build();
    }

    public List<CarPositionPriceDto> toDto(List<PositionPrice> positionPrices) {
        return positionPrices.stream().map(this::toDto).collect(Collectors.toList());
    }
}
