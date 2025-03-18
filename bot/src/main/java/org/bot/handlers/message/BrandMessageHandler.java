package org.bot.handlers.message;

import liquibase.pro.packaged.M;
import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.CarsService;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.dto.CarModelDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BrandMessageHandler implements MessageHandler {

    private final CarsService carsService;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        if (!sender.getUser().getBotState().equals(TgUser.BotState.ADD_CAR_BRAND)) {
            return false;
        }

        Optional<List<CarModelDto>> carModels = carsService.processCarBrand(sender, text);
        carModels.ifPresentOrElse(
                models -> bot.sendMessage(sender.getChatId(), MessagesConstants.INPUT_CAR_MODEL_MESSAGE, KeyboardConstants.carModelsButtons(models)),
                () -> bot.sendMessage(sender.getChatId(), MessagesConstants.NOT_FOUND_CAR_BRAND)
        );

        return true;
    }
}
