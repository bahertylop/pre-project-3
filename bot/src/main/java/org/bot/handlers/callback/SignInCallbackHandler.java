package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.UserService;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SignInCallbackHandler implements CallbackHandler {

    private final UserService userService;

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {
        if (sender.getUser().getJwtToken() != null) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.USER_ALREADY_SIGNED_IN, KeyboardConstants.botButtons());
        } else {
            userService.changeUserBotStatus(sender, TgUser.BotState.EMAIL);
            bot.sendMessage(sender.getChatId(), MessagesConstants.ADD_EMAIL_MESSAGE);
        }
    }
}
