package org.bot.service;

import liquibase.command.core.InternalGenerateChangelogCommandStep;
import liquibase.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.handlers.callback.CallbackHandler;
import org.bot.handlers.callback.ChooseModelCallbackHandler;
import org.bot.handlers.callback.GetCarPositionCallbackHandler;
import org.bot.handlers.callback.SignInCallbackHandler;
import org.bot.model.TgUser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackHandlingService {

    private final Map<String, CallbackHandler> handlersMap;

    private final UserService userService;

    public void onCallbackReceived(AvitoBot bot, Long chatId, CallbackQuery callback) {

        String callbackData = callback.getData();
        String[] dataParts = callbackData.split("_");
        Integer callbackId = callback.getMessage().getMessageId();

        Optional<TgUserDto> userOp = userService.getUserByChatId(chatId);
        if (userOp.isEmpty()) {
            // какая-то хуйня
            return;
        }

        SenderDto sender = new SenderDto(chatId, userOp.get(), callbackId);

        CallbackHandler handler = handlersMap.get(callbackToBeanName(dataParts[0]));
        if (handler != null) {
            try {
                handler.handleCallback(bot, sender, callback, dataParts);
            } catch (Exception e) {
                log.error("exception with handling callback, callbackData: {}, userId: {}", callbackData, userOp.get().getId(), e);
            }
        } else {
            log.error("undefined callback userId: {}", userOp.get().getId());
        }
    }

    private String callbackToBeanName(String callbackName) {
        String callbackNameUn = StringUtils.uncapitalize(callbackName);

        switch (callbackNameUn) {
            case "/signIn" -> {
                return StringUtils.uncapitalize(SignInCallbackHandler.class.getSimpleName());
            }
            case "/model" -> {
                return StringUtils.uncapitalize(ChooseModelCallbackHandler.class.getSimpleName());
            }
            case "/car" -> {
                return StringUtils.uncapitalize(GetCarPositionCallbackHandler.class.getSimpleName());
            }
            default -> {
                log.warn("unexpected callbackName: {}", callbackName);
                return "";
            }
        }
    }
}
