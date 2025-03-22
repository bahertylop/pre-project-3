package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.service.api.CarPositionApiService;
import org.bot.service.CarsService;
import org.bot.util.ChartUtils;
import org.bot.util.MessagesConstants;
import org.dto.response.CarPositionResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetCarPositionCallbackHandler implements CallbackHandler {

    private final CarsService carsService;

    private final CarPositionApiService carPositionApiService;

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {
        Long carPositionId = null;
        try {
            carPositionId = Long.parseLong(data[data.length - 1]);
        } catch (NumberFormatException e) {
            log.warn("undefined callback get car position: {}", callback.getData());
            bot.sendMessage(sender.getChatId(), MessagesConstants.FAILED_TO_GET_CAR_POSITION);
            return;
        }

        Optional<CarPositionResponse> carPositionResponse = carPositionApiService.getCarPosition(sender, carPositionId);
        if (carPositionResponse.isEmpty()) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.FAILED_TO_GET_CAR_POSITION);
            return;
        }
        bot.sendMessage(sender.getChatId(), MessagesConstants.getCarPositionMessage(carPositionResponse.get()));

        if (carPositionResponse.get().getPrices().isEmpty()) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.EMPTY_CAR_POSITION_PRICES_LIST);
            return;
        }

        try {
            InputFile inputFile = ChartUtils.getChartByPrices(carPositionResponse.get().getPrices());
            bot.sendPhoto(sender.getChatId(), inputFile);
        } catch (IOException e) {
            bot.sendMessage(sender.getChatId(), MessagesConstants.EMPTY_CAR_POSITION_PRICES_LIST);
            log.error("error with form chart for car position id: {} ", carPositionId, e);
        }
    }
}
