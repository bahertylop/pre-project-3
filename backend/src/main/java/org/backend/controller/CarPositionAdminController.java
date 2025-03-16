package org.backend.controller;

import lombok.RequiredArgsConstructor;
import org.backend.dto.CarPositionDto;
import org.backend.dto.response.CarPositionResponse;
import org.backend.service.CarPositionService;
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

    @GetMapping("/{positionId}")
    public CarPositionResponse getCarPosition(@PathVariable Long positionId) {
        return carPositionService.getCarPosition(positionId);
    }
}
