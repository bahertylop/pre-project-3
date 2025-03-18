package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.handlers.command.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {

    private final Map<String, CommandHandler> handlers;

    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        String commandName = command.substring(1);

        CommandHandler handler = handlers.get(commandNameToBeanName(commandName));
        if (handler != null) {
            handler.handle(bot, senderDto, commandName);
        } else {
            log.info("unexpected command: {} chatId: {}", commandName, senderDto.getChatId());
        }
    }

    private String commandNameToBeanName(String commandName) {
        String commandNameUn = StringUtils.uncapitalize(commandName);
        switch (commandNameUn) {
            case "cars" -> {
                return "carsCommandHandler";
            }
            case "add_car" -> {
                return "addCarPositionCommandHandler";
            }
            case "profile" -> {
                return "profileCommandHandler";
            }
            default -> {
                return "";
            }
        }
    }
}
