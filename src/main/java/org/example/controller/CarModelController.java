package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarModelDto;
import org.example.service.CarModelService;
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
