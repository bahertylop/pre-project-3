package org.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.AuthClient;
import org.bot.api.UserClient;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.exception.ApiException;
import org.bot.exception.AuthException;
import org.bot.exception.ForbiddenException;
import org.bot.model.TgUser;
import org.bot.repository.UserRepository;
import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.response.JwtTokensResponse;
import org.dto.response.ProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthClient authClient;

    private final UserClient userClient;

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

    public void changeUserBotStatus(SenderDto sender, TgUser.BotState newBotState) {
        TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();

        tgUser.setBotState(newBotState);
        userRepository.save(tgUser);
    }

    public boolean signInUser(SenderDto sender, String password) {
        LoginRequest loginRequest = new LoginRequest(sender.getUser().getEmail(), password);
        TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();
        JwtTokensResponse response;
        try {
            response = authClient.signIn(loginRequest);

            tgUser.setJwtToken(response.getAccess());
            tgUser.setRefreshToken(response.getRefresh());
            tgUser.setBotState(TgUser.BotState.WORKING);
            userRepository.save(tgUser);

            log.info("user email: {} signed in", tgUser.getEmail());
            return true;
        } catch (AuthException e) {
            log.warn("error with sign-in user email: {}", tgUser.getEmail(), e);
            tgUser.setBotState(TgUser.BotState.EMAIL);
            userRepository.save(tgUser);
            return false;
        }
    }

    public boolean refreshUserTokens(SenderDto sender) {
        try {
            JwtTokensResponse response = authClient.refreshTokens(new RefreshTokenRequest(sender.getUser().getRefreshToken()));

            TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();
            tgUser.setJwtToken(response.getAccess());
            tgUser.setRefreshToken(response.getRefresh());
            sender.setUser(TgUserDto.from(userRepository.save(tgUser)));
            return true;
        } catch (AuthException e) {
            log.error("tokens not refreshed", e);
            return false;
        }
    }

    public Optional<ProfileResponse> getProfileInfo(SenderDto senderDto) {
        try {
            return Optional.of(userClient.getProfileInfo(senderDto));
        } catch (ForbiddenException e) {
            log.info("forbidden for get profile info request");
            if (refreshUserTokens(senderDto)) {
                try {
                    return Optional.of(userClient.getProfileInfo(senderDto));
                } catch (ForbiddenException ex) {
                    log.error("forbidden after refresh tokens chatId: {}", senderDto.getChatId(), ex);
                } catch (ApiException ex) {
                    log.warn("api exception after forbidden chatId: {}", senderDto.getChatId(), ex);
                }
            }
        } catch (ApiException e) {
            log.warn("api exception from get profile request");
        }
        return Optional.empty();
    }
}
