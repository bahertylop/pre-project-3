package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.CarsService;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ChooseModelCallbackHandler implements CallbackHandler {

    private final CarsService carsService;

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {
        if (!sender.getUser().getBotState().equals(TgUser.BotState.ADD_CAR_MODEL)) {
            return;
        }

        String modelName = callback.getData().substring(7);
        carsService.processCarModel(sender, modelName);

        bot.deleteMessage(sender.getChatId(), callback.getMessage().getMessageId());
        bot.sendMessage(sender.getChatId(), "Выбрана модель: " + modelName + "\n" + MessagesConstants.INPUT_YEAR_FROM_MESSAGE);
    }
}
