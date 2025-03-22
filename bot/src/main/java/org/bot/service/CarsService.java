package org.bot.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.ApiProperties;
import org.bot.api.CarPositionFeignClient;
import org.bot.api.ModelsFeignClient;
import org.bot.dto.SenderDto;
import org.bot.model.CreateCarPositionData;
import org.bot.model.TgUser;
import org.bot.util.JwtTokenUtil;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.dto.response.CarPositionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarsService {

    private final ModelsFeignClient modelsFeignClient;

    private final CarPositionFeignClient carPositionFeignClient;

    private final UserService userService;

    private final CreateCarPositionDataService carPositionDataService;

    private final CarBrandService carBrandService;

    private final ApiProperties apiProperties;

    public List<CarPositionDto> getCarPositions(SenderDto senderDto) {
        try {
            return carPositionFeignClient.getCarPositions(JwtTokenUtil.bearerToken(senderDto.getUser().getJwtToken()));
        } catch (FeignException e) {
            if (e.status() == HttpStatus.FORBIDDEN.value() && userService.refreshUserTokens(senderDto)) {
                log.info("forbidden for get car positions info request");
                try {
                    return carPositionFeignClient.getCarPositions(JwtTokenUtil.bearerToken(senderDto.getUser().getJwtToken()));
                } catch (FeignException ex) {
                    if (ex.status() == HttpStatus.FORBIDDEN.value()) {
                        log.error("forbidden after refresh tokens chatId: {}", senderDto.getChatId(), ex);
                    } else {
                        log.warn("error request get car positions after forbidden chatId: {}", senderDto.getChatId(), ex);
                    }
                    return List.of();
                }
            }
            log.warn("error request get car positions chatId: {}", senderDto.getChatId(), e);
            return List.of();
        }
    }

    public Optional<CarPositionResponse> getCarPosition(SenderDto sender, Long carPositionId) {
        try {
            return Optional.of(carPositionFeignClient.getCarPosition(JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()), carPositionId));
        } catch (FeignException e) {
            if (e.status() == HttpStatus.FORBIDDEN.value() && userService.refreshUserTokens(sender)) {
                try {
                    return Optional.of(carPositionFeignClient.getCarPosition(JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()), carPositionId));
                } catch (FeignException ex) {
                    if (ex.status() == HttpStatus.FORBIDDEN.value()) {
                        log.error("forbidden after refresh tokens chatId: {}", sender.getChatId(), ex);
                    } else {
                        log.warn("error request get car position after forbidden chatId: {}", sender.getChatId(), ex);
                    }
                    return Optional.empty();
                }
            }
            log.warn("error request get car position chatId: {}", sender.getChatId(), e);
            return Optional.empty();
        }
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
            carPositionFeignClient.addCarPosition(request, JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()));
            return true;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.FORBIDDEN.value() && userService.refreshUserTokens(sender)) {
                log.info("forbidden for add car position info request");
                try {
                    carPositionFeignClient.addCarPosition(request, JwtTokenUtil.bearerToken(sender.getUser().getJwtToken()));
                    return true;
                } catch (FeignException ex) {
                    if (ex.status() == HttpStatus.FORBIDDEN.value()) {
                        log.error("forbidden after refresh tokens chatId: {}", sender.getChatId(), ex);
                    } else {
                        log.warn("error request after forbidden chatId: {}", sender.getChatId(), ex);
                    }
                    return false;
                }
            }
            log.warn("error request from get car positions chatId: {}", sender.getChatId(), e);
            return false;
        }
    }

    public List<CarBrandDto> processCarBrand(SenderDto sender, String carBrand) {
        carPositionDataService.deleteAllCarPositionDataByChatId(sender.getChatId());
        userService.changeUserBotStatus(sender, TgUser.BotState.CHOOSE_CAR_BRAND);
//        return carBrandService.getSimilarCarBrands(carBrand);
        return carBrandService.getSimilarCarBrandsUsePG(carBrand);
    }

    public Optional<List<CarModelDto>> processChooseCarBrand(SenderDto sender, Long carBrandId) {
        Optional<CarBrandDto> brandOp = carBrandService.getCarBrandById(carBrandId);
        if (brandOp.isEmpty()) {
            return Optional.empty();
        }
        carPositionDataService.createCarPositionData(sender.getChatId(), brandOp.get().getName());

        List<CarModelDto> carModels;
        try {
            carModels = modelsFeignClient.getCarModelsByBrand(brandOp.get().getId());
        } catch (FeignException e) {
            log.warn("error request get car models chatId: {}", sender.getChatId(), e);
            return Optional.empty();
        }

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
            int year = Integer.parseInt(yearFrom);
            if (year < apiProperties.getConstraint().getMinYearFrom() || year > LocalDate.now().getYear()) {
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
            int mileage = Integer.parseInt(mileageFrom);
            if (mileage < 0 || mileage > apiProperties.getConstraint().getMaxMileage()) {
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
            int mileage = Integer.parseInt(mileageBefore);
            CreateCarPositionData data = carPositionDataService.getCarPosition(sender.getChatId());
            if (mileage < 0 || mileage > apiProperties.getConstraint().getMaxMileage() ||
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
