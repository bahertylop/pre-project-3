package org.bot.handlers.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.CarsService;
import org.bot.service.UserService;
import org.bot.util.KeyboardConstants;
import org.bot.util.MessagesConstants;
import org.dto.CarModelDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChooseCarBrandCallbackHandler implements CallbackHandler {

    private final CarsService carService;

    private final UserService userService;

    @Override
    public void handleCallback(AvitoBot bot, SenderDto sender, CallbackQuery callback, String[] data) {
        Long brandId = null;
        try {
             brandId = Long.parseLong(data[data.length - 1]);
        } catch (NumberFormatException e) {
            log.warn("not number brandId in callback");
            userService.changeUserBotStatus(sender, TgUser.BotState.ADD_CAR_BRAND);
            bot.sendMessage(sender.getChatId(), MessagesConstants.INPUT_CAR_BRAND_MESSAGE);
            return;
        }

        bot.deleteMessage(sender.getChatId(), callback.getMessage().getMessageId());
        Optional<List<CarModelDto>> carModelsOp = carService.processChooseCarBrand(sender, brandId);
        carModelsOp.ifPresentOrElse(
                (models) -> bot.sendMessage(sender.getChatId(), MessagesConstants.INPUT_CAR_MODEL_MESSAGE, KeyboardConstants.carModelsButtons(models)),
                () -> bot.sendMessage(sender.getChatId(), MessagesConstants.NOT_FOUND_CAR_BRAND)
        );
    }
}
