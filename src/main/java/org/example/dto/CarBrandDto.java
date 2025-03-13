package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.CarBrand;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarBrandDto {

    private Long id;

    private String name;

    private String value;

    public static CarBrandDto from(CarBrand carBrand) {
        return CarBrandDto.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .value(carBrand.getValue())
                .build();
    }

    public static List<CarBrandDto> from(List<CarBrand> carBrands) {
        return carBrands.stream().map(CarBrandDto::from).collect(Collectors.toList());
    }
}
