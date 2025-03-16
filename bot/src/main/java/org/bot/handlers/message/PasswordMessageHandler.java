package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.UserService;
import org.bot.util.MessagesConstants;
import org.bot.util.PasswordValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordMessageHandler implements MessageHandler {

    private final UserService userService;

    private final PasswordValidator passwordValidator;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (!sender.getUser().getBotState().equals(TgUser.BotState.PASSWORD)) {
            return false;
        }

        if (!passwordValidator.isValid(text)) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.NOT_VALID_PASSWORD_MESSAGE);
            return true;
        }

        try {
            userService.signInUser(sender, text);
            bot.sendMessage(sender.getChatId(), MessagesConstants.SUCCESS_SIGN_IN);
        } catch (Exception e) {
            log.error("не получилось авторизовать пользователя");
        }
        return true;
    }
}
