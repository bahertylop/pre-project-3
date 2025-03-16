package org.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.CarModel;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarModelDto {

    private String name;

    private String value;

    public static CarModelDto from(CarModel carModel) {
        return CarModelDto.builder()
                .name(carModel.getName())
                .value(carModel.getValue())
                .build();
    }

    public static List<CarModelDto> from(List<CarModel> carModels) {
        return carModels.stream().map(CarModelDto::from).collect(Collectors.toList());
    }
}
