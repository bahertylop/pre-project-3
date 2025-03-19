package org.bot.service;

import liquibase.pro.packaged.B;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.CarPositionClient;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.bot.model.CarBrand;
import org.bot.model.CreateCarPositionData;
import org.bot.model.TgUser;
import org.bot.util.MessagesConstants;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.dto.response.CarPositionResponse;
import org.hibernate.annotations.Target;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarPositionClient carPositionClient;

    private final UserService userService;

    private final CreateCarPositionDataService carPositionDataService;

    private final CarBrandService carBrandService;

    @Value("${api.constraint.min-year-from}")
    private Integer minYearFromParam;

    @Value("${api.constraint.max-mileage}")
    private Integer maxMileageParam;

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

        try {
            carPositionClient.addCarPosition(sender, request);
            return true;
        } catch (ForbiddenException e) {
            if (userService.refreshUserTokens(sender)) {
                try {
                    carPositionClient.addCarPosition(sender, request);
                    return true;
                } catch (ForbiddenException ex) {
                    log.error("forbidden after refresh tokens chatId: {}", sender.getChatId(), ex);
                }
            }
        } catch (ApiException e) {
            log.warn("api exception from get car positions request");
        }
        return false;
    }

    public Optional<CarPositionResponse> getCarPosition(SenderDto sender, Long carPositionId) {
        try {
            return Optional.of(carPositionClient.getCarPosition(sender, carPositionId));
        } catch (ForbiddenException e) {
            if (userService.refreshUserTokens(sender)) {
                try {
                    return Optional.of(carPositionClient.getCarPosition(sender, carPositionId));
                } catch (ForbiddenException ex) {
                    log.error("forbidden after refresh tokens chatId: {}", sender.getChatId(), ex);
                }
            }
        } catch (ApiException e) {
            log.warn("api exception from get car position request");
        }
        return Optional.empty();
    }

    public List<CarBrandDto> processCarBrand(SenderDto sender, String carBrand) {
        carPositionDataService.deleteAllCarPositionDataByChatId(sender.getChatId());
        userService.changeUserBotStatus(sender, TgUser.BotState.CHOOSE_CAR_BRAND);
        return carBrandService.getSimilarCarBrands(carBrand);
    }

    public Optional<List<CarModelDto>> processChooseCarBrand(SenderDto sender, Long carBrandId) {
        Optional<CarBrandDto> brandOp = carBrandService.getCarBrandById(carBrandId);
        if (brandOp.isEmpty()) {
            return Optional.empty();
        }
        carPositionDataService.createCarPositionData(sender.getChatId(), brandOp.get().getName());

        List<CarModelDto> carModels = carPositionClient.getCarModels(brandOp.get());
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
        userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_YEAR_FROM);
    }

    public boolean processYearFrom(SenderDto sender, String yearFrom) {
        if (yearFrom.equals("-")) {
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_YEAR_BEFORE);
            return true;
        }
        try {
            Integer year = Integer.parseInt(yearFrom);
            if (year < minYearFromParam || year > LocalDate.now().getYear()) {
                log.debug("user input not valid yearFrom: {}", year);
                return false;
            }
            carPositionDataService.setYearFromToCarCreatePositionData(sender.getChatId(), year);
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_YEAR_BEFORE);
            return true;
        } catch (NumberFormatException e) {
            log.debug("user input not int yearFrom");
            return false;
        }
    }

    public boolean processYearBefore(SenderDto sender, String yearBefore) {
        if (yearBefore.equals("-")) {
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_MILEAGE_FROM);
            return true;
        }
        try {
            Integer year = Integer.parseInt(yearBefore);
            CreateCarPositionData data = carPositionDataService.getCarPosition(sender.getChatId());
            if (data.getYearFrom() != null && data.getYearFrom() > year ||
                year > LocalDate.now().getYear()) {
                log.debug("user input not valid yearBefore: {}", year);
                return false;
            }

            carPositionDataService.setYearBeforeToCarCreatePositionData(sender.getChatId(), year);
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_MILEAGE_FROM);
            return true;
        } catch (NumberFormatException e) {
            log.debug("user input not int yearBefore");
            return false;
        }
    }

    public boolean processMileageFrom(SenderDto sender, String mileageFrom) {
        if (mileageFrom.equals("-")) {
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_MILEAGE_BEFORE);
            return true;
        }
        try {
            Integer mileage = Integer.parseInt(mileageFrom);
            if (mileage < 0 || mileage > maxMileageParam) {
                log.debug("user input negative mileageFrom");
                return false;
            }

            carPositionDataService.setMileageFromToCarCreatePositionData(sender.getChatId(), mileage);
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_PARAM_MILEAGE_BEFORE);
            return true;
        } catch (NumberFormatException e) {
            log.debug("user input not int mileageFrom");
            return false;
        }

    }

    public boolean processMileageBefore(SenderDto sender, String mileageBefore) {
        if (mileageBefore.equals("-")) {
            userService.changeUserBotStatus(sender, TgUser.BotState.WORKING);
            return true;
        }
        try {
            Integer mileage = Integer.parseInt(mileageBefore);
            CreateCarPositionData data = carPositionDataService.getCarPosition(sender.getChatId());
            if (mileage < 0 || mileage > maxMileageParam ||
                data.getMileageFrom() != null && data.getMileageFrom() > mileage
            ) {
                log.debug("user input not valid yearBefore: {}", mileage);
                return false;
            }

            carPositionDataService.setMileageBeforeToCarCreatePositionData(sender.getChatId(), mileage);
            userService.changeUserBotStatus(sender, TgUser.BotState.WORKING);
            return true;
        } catch (NumberFormatException e) {
            log.debug("user input not int yearBefore");
            return false;
        }
    }
}
