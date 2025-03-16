package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.model.TgUser;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextMessageHandlingService {

    private final UserService userService;

    private final MessageService messageService;

    public void onMessageReceived(AvitoBot bot, Long chatId, Message message) {
        String text = message.getText();
        Integer messageId = message.getMessageId();

        Optional<TgUserDto> tgUserOp = userService.getUserByChatId(chatId);
        if (tgUserOp.isEmpty()) {
            userService.addNewUser(chatId, message.getFrom().getUserName());
            bot.sendMessage(chatId, MessagesConstants.HELLO_MESSAGE, KeyboardConstants.authCommands());
            return;
        }

        SenderDto sender = new SenderDto(chatId, tgUserOp.get(), messageId);

        if (message.isCommand()) {
            // парсинг команд
            return;
        }

        messageService.handleMessage(bot, sender, text);
    }
}
