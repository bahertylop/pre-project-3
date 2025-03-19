package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.service.CarsService;
import org.dto.CarPositionDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GetCarPositionCallbackHandler implements CallbackHandler {

    private final CarsService carsService;

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {

    }
}
