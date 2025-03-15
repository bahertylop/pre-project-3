package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CarPositionDto;
import org.example.dto.request.CreateCarPositionRequest;
import org.example.dto.response.CarPositionResponse;
import org.example.service.CarPositionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarPositionController {

    private final CarPositionService carPositionService;

    @GetMapping
    public List<CarPositionDto> getCarPositions(@AuthenticationPrincipal UserDetails userDetails) {
        return carPositionService.getUserCars(userDetails);
    }

    @GetMapping("/{carPositionId}")
    public CarPositionResponse getUserCarPosition(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long carPositionId) {
        return carPositionService.getUserCarPosition(userDetails, carPositionId);
    }

    @PostMapping
    public void createCarPosition(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody @Validated CreateCarPositionRequest request) {
        carPositionService.createCarPosition(userDetails, request);
    }

    @DeleteMapping("/{carPositionId}")
    public void deleteCarPosition(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long carPositionId) {
        carPositionService.deleteCarPosition(userDetails, carPositionId);
    }
}
