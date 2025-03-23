package org.bot.service.api;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.ModelsFeignClient;
import org.bot.dto.SenderDto;
import org.dto.CarModelDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelsApiService {

    private final ModelsFeignClient modelsFeignClient;

    public List<CarModelDto> getCarModelsByBrand(SenderDto sender, Long carBrandId) {
        List<CarModelDto> carModels;
        try {
            return modelsFeignClient.getCarModelsByBrand(carBrandId);

        } catch (FeignException e) {
            log.warn("error request get car models chatId: {}", sender.getChatId(), e);
            return List.of();
        }
    }
}
