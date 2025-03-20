package org.bot.handlers.command;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.CarsService;
import org.bot.service.UserService;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AddCarPositionCommandHandler implements CommandHandler {

    private final UserService userService;

    @Override
    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        userService.changeUserBotStatus(senderDto, TgUser.BotState.ADD_CAR_BRAND);
        bot.sendMessage(senderDto.getChatId(), MessagesConstants.INPUT_CAR_BRAND_MESSAGE);
    }
}
