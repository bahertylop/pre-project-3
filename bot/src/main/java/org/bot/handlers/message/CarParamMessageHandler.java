package org.bot.handlers.message;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.CarsService;
import org.bot.util.MessagesConstants;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class CarParamMessageHandler implements MessageHandler {

    private final Map<TgUser.BotState, Consumer<HandlerArgs>> handlers = Map.of(
            TgUser.BotState.ADD_CAR_PARAM_YEAR_FROM, this::handleAddYearFrom,
            TgUser.BotState.ADD_CAR_PARAM_YEAR_BEFORE, this::handleAddYearBefore,
            TgUser.BotState.ADD_CAR_PARAM_MILEAGE_FROM, this::handleAddMileageFrom,
            TgUser.BotState.ADD_CAR_PARAM_MILEAGE_BEFORE, this::handleAddMileageBefore
    );

    private final CarsService carsService;

    @Override
    public boolean handleMessage(AvitoBot bot, SenderDto sender, String text) {
        Consumer<HandlerArgs> handleMethod = handlers.get(sender.getUser().getBotState());
        if (handleMethod != null) {
            handleMethod.accept(new HandlerArgs(bot, sender, text));
            return true;
        }

        return false;
    }

    public void handleAddYearFrom(HandlerArgs args) {
        if (args.text.equals("-") || carsService.processYearFrom(args.sender, args.text)) {
            args.bot.sendMessage(args.sender.getChatId(), MessagesConstants.INPUT_YEAR_BEFORE_MESSAGE);
            return;
        }
        args.bot.sendMessage(args.sender.getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_YEAR_FROM_MESSAGE);
    }

    public void handleAddYearBefore(HandlerArgs args) {
        if (args.text.equals("-") || carsService.processYearBefore(args.sender, args.text)) {
            args.bot.sendMessage(args.sender.getChatId(), MessagesConstants.INPUT_MILEAGE_FROM_MESSAGE);
            return;
        }
        args.bot.sendMessage(args.sender.getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_YEAR_BEFORE_MESSAGE);
    }

    public void handleAddMileageFrom(HandlerArgs args) {
        if (args.text.equals("-") || carsService.processMileageFrom(args.sender, args.text)) {
            args.bot.sendMessage(args.sender.getChatId(), MessagesConstants.INPUT_MILEAGE_BEFORE_MESSAGE);
            return;
        }
        args.bot.sendMessage(args.sender.getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_MILEAGE_FROM_MESSAGE);
    }

    public void handleAddMileageBefore(HandlerArgs args) {
        if (args.text.equals("-") || carsService.processMileageBefore(args.sender, args.text)) {

            if (carsService.createCarPosition(args.sender)) {
                args.bot.sendMessage(args.sender.getChatId(), MessagesConstants.SUCCESS_ADD_CAR_POSITION);
            } else {
                args.bot.sendMessage(args.sender.getChatId(), MessagesConstants.FAILED_TO_ADD_CAR_POSITION);
            }
            return;
        }
        args.bot.sendMessage(args.sender.getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_MILEAGE_BEFORE_MESSAGE);
    }

    private record HandlerArgs(AvitoBot bot, SenderDto sender, String text) {}
}
