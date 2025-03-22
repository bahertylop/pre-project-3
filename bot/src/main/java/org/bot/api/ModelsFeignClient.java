package org.bot.api;

import org.dto.CarModelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "modelsClient", url = "${api.url.base-url}")
public interface ModelsFeignClient {

    @GetMapping(value = "${api.url.get-car-models}")
    List<CarModelDto> getCarModelsByBrand(@RequestParam(name = "brandId") Long carBrandId);
}
