package org.bot.handlers.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.api.CarPositionApiService;
import org.bot.service.CarsService;
import org.bot.util.MessagesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class CarParamMessageHandler implements MessageHandler {

    private final Map<TgUser.BotState, Consumer<HandlerArgs>> handlers;

    private final CarsService carsService;

    private final CarPositionApiService carPositionApiService;

    @Autowired
    public CarParamMessageHandler(CarsService carsService, CarPositionApiService carPositionApiService) {
        Map<TgUser.BotState, Consumer<HandlerArgs>> tempHandlers = new HashMap<>();
        tempHandlers.put(TgUser.BotState.ADD_CAR_PARAM_YEAR_FROM, this::handleAddYearFrom);
        tempHandlers.put(TgUser.BotState.ADD_CAR_PARAM_YEAR_BEFORE, this::handleAddYearBefore);
        tempHandlers.put(TgUser.BotState.ADD_CAR_PARAM_MILEAGE_FROM, this::handleAddMileageFrom);
        tempHandlers.put(TgUser.BotState.ADD_CAR_PARAM_MILEAGE_BEFORE, this::handleAddMileageBefore);
        handlers = tempHandlers;
        this.carsService = carsService;
        this.carPositionApiService = carPositionApiService;
    }

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
        if (carsService.processYearFrom(args.getSender(), args.getText())) {
            args.getBot().sendMessage(args.getSender().getChatId(), MessagesConstants.INPUT_YEAR_BEFORE_MESSAGE);
            return;
        }
        args.getBot().sendMessage(args.getSender().getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_YEAR_FROM_MESSAGE);
    }

    public void handleAddYearBefore(HandlerArgs args) {
        if (carsService.processYearBefore(args.getSender(), args.getText())) {
            args.getBot().sendMessage(args.getSender().getChatId(), MessagesConstants.INPUT_MILEAGE_FROM_MESSAGE);
            return;
        }
        args.getBot().sendMessage(args.getSender().getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_YEAR_BEFORE_MESSAGE);
    }

    public void handleAddMileageFrom(HandlerArgs args) {
        if (carsService.processMileageFrom(args.getSender(), args.getText())) {
            args.getBot().sendMessage(args.getSender().getChatId(), MessagesConstants.INPUT_MILEAGE_BEFORE_MESSAGE);
            return;
        }
        args.getBot().sendMessage(args.getSender().getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_MILEAGE_FROM_MESSAGE);
    }

    public void handleAddMileageBefore(HandlerArgs args) {
        if (carsService.processMileageBefore(args.getSender(), args.getText())) {
            if (carPositionApiService.createCarPosition(args.getSender())) {
                args.getBot().sendMessage(args.getSender().getChatId(), MessagesConstants.SUCCESS_ADD_CAR_POSITION);
            } else {
                args.getBot().sendMessage(args.getSender().getChatId(), MessagesConstants.FAILED_TO_ADD_CAR_POSITION);
            }
            return;
        }
        args.getBot().sendMessage(args.getSender().getChatId(), "Ошибка!\n" + MessagesConstants.INPUT_MILEAGE_BEFORE_MESSAGE);
    }

    @Data
    @AllArgsConstructor
    private static class HandlerArgs {
        private final AvitoBot bot;
        private final SenderDto sender;
        private final String text;
    }
}
