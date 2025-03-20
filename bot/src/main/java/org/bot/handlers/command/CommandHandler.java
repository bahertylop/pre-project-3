package org.bot.handlers.command;

import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;

public interface CommandHandler {

    void handle(AvitoBot bot, SenderDto senderDto, String command);
}
