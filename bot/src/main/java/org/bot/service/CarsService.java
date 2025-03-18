package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.CarPositionClient;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.bot.model.CreateCarPositionData;
import org.bot.model.TgUser;
import org.bot.repository.CreateCarPositionDataRepository;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarPositionClient carPositionClient;

    private final UserService userService;

    private final CreateCarPositionDataService carPositionDataService;

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

        carPositionDataService.deleteAllCarPositionDataByChatId(sender.getChatId());
        carPositionDataService.createCarPositionData(sender.getChatId(), foundCarBrand.get().getName());

        List<CarModelDto> carModels = carPositionClient.getCarModels(foundCarBrand.get());
        userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_MODEL);
        return Optional.of(carModels);
    }

    private Optional<CarBrandDto> findCarBrandName(List<CarBrandDto> carBrands, String inputCarBrand) {
        return carBrands.stream()
                .filter(brand -> brand.getName().equalsIgnoreCase(inputCarBrand))
                .findFirst();
    }

    public void processCarModel(SenderDto sender, String carModelName) {
        carPositionDataService.setModelToCarCreatePositionData(sender.getChatId(), carModelName);
        userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAMS);
    }
}
