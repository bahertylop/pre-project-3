package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.handlers.command.ProfileCommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
public class ProfileMessageHandler implements MessageHandler {

    private final ProfileCommandHandler profileCommandHandler;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (text.equals("профиль")) {
            profileCommandHandler.handle(bot, sender, "/profile");
            return true;
        }

        return false;
    }
}
