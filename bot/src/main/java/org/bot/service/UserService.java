package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.model.TgUser;
import org.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<TgUserDto> getUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId).map(TgUserDto::from);
    }

    private Optional<TgUser> getTgUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId);
    }

    public void addNewUser(Long chatId, String userName) {
        if (userRepository.getUserByChatId(chatId).isPresent()) {
            // пользователь уже существует
            return;
        }

        TgUser newTgUser = TgUser.builder()
                .chatId(chatId)
                .tgUserName(userName)
                .botState(TgUser.BotState.EMAIL)
                .build();

        userRepository.save(newTgUser);
    }

    public void addEmailToUser(SenderDto sender, String email) {
        Optional<TgUser> tgUserOp = getTgUserByChatId(sender.getChatId());
        if (tgUserOp.isEmpty()) {
            // не может такого быть по сути
            return;
        }

        TgUser user = tgUserOp.get();
        user.setEmail(email);
        user.setBotState(TgUser.BotState.PASSWORD);
        userRepository.save(user);
    }

    public void signInUser(SenderDto sender, String password) {
        log.info("типо авторизация");
    }
}
