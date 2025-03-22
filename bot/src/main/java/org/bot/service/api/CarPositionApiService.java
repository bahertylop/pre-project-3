package org.bot.service.api;

import lombok.RequiredArgsConstructor;
import org.bot.api.CarPositionFeignClient;
import org.bot.dto.SenderDto;
import org.bot.model.CreateCarPositionData;
import org.bot.service.CreateCarPositionDataService;
import org.bot.util.JwtTokenUtil;
import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.dto.response.CarPositionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarPositionApiService {

    private final RetryService retryService;

    private final CreateCarPositionDataService carPositionDataService;

    private final CarPositionFeignClient carPositionFeignClient;

    public List<CarPositionDto> getCarPositions(SenderDto sender) {
        return retryService.executeWithRetry(
                () -> carPositionFeignClient.getCarPositions(JwtTokenUtil.bearerToken(sender.getUser().getJwtToken())),
                sender,
                List.of()
        );
    }

    public Optional<CarPositionResponse> getCarPosition(SenderDto sender, Long carPositionId) {
        return Optional.ofNullable(retryService.executeWithRetry(
                () -> carPositionFeignClient.getCarPosition(JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()), carPositionId),
                sender,
                null
        ));
    }

    public boolean createCarPosition(SenderDto sender) {
        CreateCarPositionData data = carPositionDataService.getCarPosition(sender.getChatId());
        CreateCarPositionRequest request = CreateCarPositionRequest.builder()
                .brand(data.getBrandName())
                .model(data.getModelName())
                .yearFrom(data.getYearFrom())
                .yearBefore(data.getYearTo())
                .mileageFrom(data.getMileageFrom())
                .mileageBefore(data.getMileageBefore())
                .build();

        return retryService.executeWithRetry(
                () -> {
                    carPositionFeignClient.addCarPosition(request, JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()));
                    return true;
                },
                sender,
                false
        );
    }
}
