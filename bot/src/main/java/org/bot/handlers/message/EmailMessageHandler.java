package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.UserService;
import org.bot.util.EmailValidator;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageHandler implements MessageHandler {

    private final UserService userService;

    private final EmailValidator emailValidator;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (!sender.getUser().getBotState().equals(TgUser.BotState.EMAIL)) {
            return false;
        }

        if (!emailValidator.isValid(text)) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.NOT_VALID_EMAIL_MESSAGE);
            return true;
        }

        try {
            userService.addEmailToUser(sender, text);
            bot.sendMessage(sender.getChatId(), MessagesConstants.ADD_PASSWORD_MESSAGE);
        } catch (Exception e) {
            log.error("не получилось сохранить почту в базе", e);
        }
        return true;
    }
}
