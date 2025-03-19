package org.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.PositionPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarPositionPriceDto {

    private LocalDate date;

    private Integer price;

    public static CarPositionPriceDto from(PositionPrice positionPrice) {
        return CarPositionPriceDto.builder()
                .date(positionPrice.getDate())
                .price(positionPrice.getPrice())
                .build();
    }

    public static List<CarPositionPriceDto> from(List<PositionPrice> prices) {
        return prices.stream().map(CarPositionPriceDto::from).collect(Collectors.toList());
    }
}
