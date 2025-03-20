package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.handlers.command.AddCarPositionCommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
public class AddCarPositionMessageHandler implements MessageHandler {

    private final AddCarPositionCommandHandler addCarPositionCommandHandler;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (text.equals("добавить машину")) {
            addCarPositionCommandHandler.handle(bot, sender, "/add_car");
            return true;
        }
        return false;
    }
}
