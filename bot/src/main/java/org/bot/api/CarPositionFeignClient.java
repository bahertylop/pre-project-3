package org.bot.api;

import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.dto.response.CarPositionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "carPositionClient", url = "${api.url.base-url}")
public interface CarPositionFeignClient {

    @GetMapping(value = "${api.url.car-positions}")
    List<CarPositionDto> getCarPositions(@RequestHeader("Authorization") String tokenHeader);

    @GetMapping(value = "${api.url.car-positions}/{id}")
    CarPositionResponse getCarPosition(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id);

    @PostMapping(value = "${api.url.car-positions}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addCarPosition(@RequestBody CreateCarPositionRequest request, @RequestHeader("Authorization") String tokenHeader);
}
