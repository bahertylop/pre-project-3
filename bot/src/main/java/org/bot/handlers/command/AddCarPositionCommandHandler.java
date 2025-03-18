package org.bot.handlers.command;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.service.CarsService;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AddCarPositionCommandHandler implements CommandHandler {

    private final CarsService carsService;

    @Override
    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        // заебашить обработку добавления
    }
}
