package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.AuthClient;
import org.bot.api.CarPositionClient;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.AuthException;
import org.bot.exception.ForbiddenException;
import org.dto.CarPositionDto;
import org.dto.request.RefreshTokenRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
