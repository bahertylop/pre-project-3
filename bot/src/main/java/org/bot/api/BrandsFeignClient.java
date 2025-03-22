package org.bot.api;

import org.dto.CarBrandDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "brandsClient", url = "${api.url.base-url}")
public interface BrandsFeignClient {

    @GetMapping(value = "${api.url.get-car-brands}")
    List<CarBrandDto> getCarBrands(@RequestHeader("Authorization") String accessToken);
}
