package org.bot.handlers.command;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultCommandHandler implements CommandHandler {

    @Override
    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        if (command.equals("/start")) {
            bot.sendMessage(senderDto.getChatId(), MessagesConstants.USER_ALREADY_SIGNED_IN, KeyboardConstants.botButtons());
            return;
        }

        bot.sendMessage(senderDto.getChatId(), MessagesConstants.UNDEFINED_COMMAND_MESSAGE, KeyboardConstants.botButtons());
    }
}
