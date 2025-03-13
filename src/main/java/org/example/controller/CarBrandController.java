package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarBrandDto;
import org.example.service.CarBrandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class CarBrandController {

    private final CarBrandService carBrandService;

    @GetMapping
    public List<CarBrandDto> getCarBrands() {
        return carBrandService.getCarBrands();
    }
}
