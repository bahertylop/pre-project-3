package org.bot.handlers.command;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.api.CarPositionApiService;
import org.bot.service.UserService;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.dto.CarPositionDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CarsCommandHandler implements CommandHandler {

    private final CarPositionApiService carPositionApiService;

    private final UserService userService;

    @Override
    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        userService.changeUserBotStatus(senderDto, TgUser.BotState.WORKING);

        List<CarPositionDto> carPositions = carPositionApiService.getCarPositions(senderDto);
        if (carPositions.isEmpty()) {
            bot.sendMessage(senderDto.getChatId(), MessagesConstants.EMPTY_CAR_POSITION_LIST);
        } else {
            bot.sendMessage(senderDto.getChatId(), MessagesConstants.carPositionListMessage(carPositions), KeyboardConstants.carPositionListButtons(carPositions));
        }
    }
}
