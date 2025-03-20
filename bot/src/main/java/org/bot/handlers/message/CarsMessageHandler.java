package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.handlers.command.CarsCommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
public class CarsMessageHandler implements MessageHandler {

    private final CarsCommandHandler carsCommandHandler;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (text.equals("машины")) {
            carsCommandHandler.handle(bot, sender, "/cars");
            return true;
        }
        return false;
    }
}
