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

    private final AuthFeignClient authFeignClient;

    private final ProfileFeignClient profileFeignClient;

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

    @Transactional
    public void changeUserBotStatus(SenderDto sender, TgUser.BotState newBotState) {
        TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();

        tgUser.setBotState(newBotState);
        userRepository.save(tgUser);
    }

    public boolean signInUser(SenderDto sender, String password) {
        LoginRequest loginRequest = new LoginRequest(sender.getUser().getEmail(), password);
        TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();

        try {
            JwtTokensResponse response = authFeignClient.signInUser(loginRequest);

            tgUser.setJwtToken(response.getAccess());
            tgUser.setRefreshToken(response.getRefresh());
            tgUser.setBotState(TgUser.BotState.WORKING);
            userRepository.save(tgUser);

            log.info("user email: {} signed in", tgUser.getEmail());
            return true;
        } catch (FeignException e) {
            log.warn("error with sign-in user email: {}", tgUser.getEmail(), e);
            tgUser.setBotState(TgUser.BotState.EMAIL);
            userRepository.save(tgUser);
            return false;
        }
    }

    public boolean refreshUserTokens(SenderDto sender) {
        try {
            JwtTokensResponse response = authFeignClient.refreshTokens(new RefreshTokenRequest(sender.getUser().getRefreshToken()));

            TgUser tgUser = getTgUserByChatId(sender.getChatId()).get();
            tgUser.setJwtToken(response.getAccess());
            tgUser.setRefreshToken(response.getRefresh());
            sender.setUser(TgUserDto.from(userRepository.save(tgUser)));
            return true;
        } catch (FeignException e) {
            log.error("tokens not refreshed", e);
            return false;
        }
    }

    public Optional<ProfileResponse> getProfileInfo(SenderDto senderDto) {
        try {
            return Optional.of(profileFeignClient.getProfileInfo(JwtTokenUtil.bearerToken(senderDto.getUser().getJwtToken())));
        } catch (FeignException e) {
            if (e.status() == HttpStatus.FORBIDDEN.value() && refreshUserTokens(senderDto)) {
                log.info("forbidden for get profile info request");
                try {
                    return Optional.of(profileFeignClient.getProfileInfo(JwtTokenUtil.bearerToken(senderDto.getUser().getJwtToken())));
                } catch (FeignException ex) {
                    if (ex.status() == HttpStatus.FORBIDDEN.value()) {
                        log.error("forbidden after refresh tokens chatId: {}", senderDto.getChatId(), ex);
                    } else {
                        log.warn("error request after forbidden chatId: {}", senderDto.getChatId(), ex);
                    }
                    return Optional.empty();
                }
            }
            log.warn("error request get profile chatId: {}", senderDto.getChatId(), e);
            return Optional.empty();
        }
    }
}
