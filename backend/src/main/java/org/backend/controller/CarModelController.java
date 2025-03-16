package org.backend.controller;

import lombok.RequiredArgsConstructor;
import org.backend.dto.CarModelDto;
import org.backend.service.CarModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/models")
public class CarModelController {

    private final CarModelService carModelService;

    @GetMapping
    public List<CarModelDto> getCarModelByBrand(@RequestParam Long brandId) {
        return carModelService.getCarModelsByBrand(brandId);
    }
}
