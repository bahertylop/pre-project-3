package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SignInCallbackHandler implements CallbackHandler {

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {
        bot.sendMessage(sender.getChatId(), MessagesConstants.ADD_EMAIL_MESSAGE);
    }
}
