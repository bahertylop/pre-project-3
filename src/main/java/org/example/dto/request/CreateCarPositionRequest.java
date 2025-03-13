package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCarPositionRequest {

    private String brand;

    private String model;

    private Integer yearFrom;

    private Integer yearBefore;

    private Integer mileageFrom;

    private Integer mileageBefore;
}
