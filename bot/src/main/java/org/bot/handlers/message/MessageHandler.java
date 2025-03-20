package org.bot.handlers.message;

import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;

public interface MessageHandler {

    boolean handleMessage(AvitoBot bot, SenderDto sender, String text);
}
