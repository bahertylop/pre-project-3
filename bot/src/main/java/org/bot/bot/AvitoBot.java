package org.bot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.model.TgUser;
import org.bot.service.CallbackHandlingService;
import org.bot.service.TextMessageHandlingService;
import org.bot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvitoBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;

    private final TextMessageHandlingService textMessageHandlingService;

    private final CallbackHandlingService callbackHandlingService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            User user = message.getFrom();
            if (user.getIsBot()) {
                return;
            }

            textMessageHandlingService.onMessageReceived(this, chatId, message);
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long chatId = callbackQuery.getFrom().getId();

            callbackHandlingService.onCallbackReceived(this, chatId, callbackQuery);
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("не удалось отправить сообщение, chatId: {}", chatId);
        }
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(keyboard);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("не удалось отправить сообщение, chatId: {}", chatId);
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
