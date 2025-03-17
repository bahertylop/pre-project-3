package org.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarPositionRequest {

    @NotBlank(message = "Марка не может быть пустой")
    private String brand;

    @NotBlank(message = "Модель автомобиля не может быть пустой")
    private String model;

    @Min(value = 1900, message = "Год начала не может быть раньше 1900")
    @Max(value = 2025, message = "Год начала не может быть позже 2025")
    private Integer yearFrom;

    @Min(value = 1900, message = "Год начала не может быть раньше 1900")
    @Max(value = 2025, message = "Год начала не может быть позже 2025")
    private Integer yearBefore;

    @Min(value = 0, message = "Пробег не может быть отрицательным")
    private Integer mileageFrom;

    @Min(value = 0, message = "Пробег не может быть отрицательным")
    private Integer mileageBefore;

    @AssertTrue(message = "Год начала не может быть больше года окончания")
    public boolean isYearRangeValid() {
        return yearFrom == null || yearBefore == null || yearFrom <= yearBefore;
    }

    @AssertTrue(message = "Пробег от не может быть больше пробега до")
    public boolean isMileageRangeValid() {
        return mileageFrom == null || mileageBefore == null || mileageFrom <= mileageBefore;
    }
}
