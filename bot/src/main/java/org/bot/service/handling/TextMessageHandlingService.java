package org.bot.service.handling;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.service.UserService;
import org.bot.service.api.AuthService;
import org.bot.service.handling.CommandService;
import org.bot.service.handling.MessageService;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextMessageHandlingService {

    private final UserService userService;

    private final MessageService messageService;

    private final CommandService commandsService;

    private final AuthService authService;

    public void onMessageReceived(AvitoBot bot, Long chatId, Message message) {
        String text = message.getText();
        Integer messageId = message.getMessageId();

        Optional<TgUserDto> tgUserOp = userService.getUserByChatId(chatId);
        if (tgUserOp.isEmpty()) {
            userService.addNewUser(chatId, message.getFrom().getUserName());
            authService.signInUser(chatId);
            bot.sendMessage(chatId, MessagesConstants.HELLO_MESSAGE, KeyboardConstants.botButtons());
            return;
        }

        SenderDto sender = new SenderDto(chatId, tgUserOp.get(), messageId);

        if (message.isCommand()) {
            commandsService.handle(bot, sender, message.getText());
            return;
        }

        messageService.handleMessage(bot, sender, text);
    }
}
