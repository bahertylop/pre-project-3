package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.handlers.message.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final List<MessageHandler> messageHandlers;

    public void handleMessage(AvitoBot bot, SenderDto sender, String text) {
        for (MessageHandler handler : messageHandlers) {
            if (handler.handleMessage(bot, sender, text)) {
                return;
            }
        }
    }
}
