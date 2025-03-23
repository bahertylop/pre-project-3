package org.bot.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.AuthFeignClient;
import org.bot.api.ProfileFeignClient;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.model.TgUser;
import org.bot.repository.UserRepository;
import org.bot.util.JwtTokenUtil;
import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.response.JwtTokensResponse;
import org.dto.response.ProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<TgUserDto> getUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId).map(TgUserDto::from);
    }

    public Optional<TgUser> getTgUserByChatId(Long chatId) {
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
                .botState(TgUser.BotState.WORKING)
                .build();

        userRepository.save(newTgUser);
    }

    @Transactional
    public void changeUserBotStatus(SenderDto sender, TgUser.BotState newBotState) {
        TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();

        tgUser.setBotState(newBotState);
        userRepository.save(tgUser);
    }

    public TgUserDto setUserTokens(String accessToken, String refreshToken, Long chatId) {
        TgUser tgUser = getTgUserByChatId(chatId).get();

        tgUser.setJwtToken(accessToken);
        tgUser.setRefreshToken(refreshToken);

        return TgUserDto.from(userRepository.save(tgUser));
    }

    public TgUserDto setUserTokensBotAndStatus(String accessToken, String refreshToken, Long chatId, TgUser.BotState botState) {
        TgUser tgUser = getTgUserByChatId(chatId).get();

        tgUser.setJwtToken(accessToken);
        tgUser.setRefreshToken(refreshToken);
        tgUser.setBotState(botState);

        return TgUserDto.from(userRepository.save(tgUser));
    }
}
