package org.bot.handlers.callback;

import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {

    void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data);
}
