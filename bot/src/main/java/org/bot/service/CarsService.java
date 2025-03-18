package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.AuthClient;
import org.bot.api.CarPositionClient;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.AuthException;
import org.bot.exception.ForbiddenException;
import org.bot.model.TgUser;
import org.bot.util.MessagesConstants;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.dto.request.RefreshTokenRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarPositionClient carPositionClient;

    private final UserService userService;

    public List<CarPositionDto> getCarPositions(SenderDto senderDto) {
        System.out.println(senderDto.getUser().getJwtToken());
        try {
            return carPositionClient.getCarPositions(senderDto);
        } catch (ForbiddenException e) {
            if (userService.refreshUserTokens(senderDto)) {
                try {
                    System.out.println(senderDto.getUser().getJwtToken());
                    return carPositionClient.getCarPositions(senderDto);
                } catch (ForbiddenException ex) {
                    log.error("forbidden after refresh tokens chatId: {}", senderDto.getChatId(), ex);
                } catch (ApiException exApi) {
                    log.warn("api exception after forbidden chatId: {}", senderDto.getChatId(), exApi);
                }
            }
        } catch (ApiException e) {
            log.warn("api exception from get car positions request");
        }
        return List.of();
    }

    public Optional<List<CarModelDto>> processCarBrand(SenderDto sender, String carBrand) {
        List<CarBrandDto> carBrands = carPositionClient.getCarBrands();

        Optional<CarBrandDto> foundCarBrand = findCarBrandName(carBrands, carBrand);
        if (foundCarBrand.isEmpty()) {
            return Optional.empty();
        }

        List<CarModelDto> carModels = carPositionClient.getCarModels(foundCarBrand.get());
        userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_MODEL);
        return Optional.of(carModels);
    }

    private Optional<CarBrandDto> findCarBrandName(List<CarBrandDto> carBrands, String inputCarBrand) {
        List<String> carBrandNamesLow = carBrands.stream().map(CarBrandDto::getName).map(String::toLowerCase).toList();

        for (int i = 0; i < carBrandNamesLow.size(); i++) {
            if (carBrandNamesLow.get(i).equals(inputCarBrand.toLowerCase())) {
                return Optional.of(carBrands.get(i));
            }
        }
        return Optional.empty();
    }
}
