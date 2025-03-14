package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarPositionDto;
import org.example.dto.response.CarPositionResponse;
import org.example.service.CarPositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cars")
public class CarPositionAdminController {

    private final CarPositionService carPositionService;

    @GetMapping
    public List<CarPositionDto> getUserCarsByAdmin(@RequestParam Long userId) {
        return carPositionService.getUserCars(userId);
    }

    @GetMapping("/car")
    public CarPositionResponse getCarPosition(@RequestParam Long positionId) {
        return carPositionService.getCarPosition(positionId);
    }
}
