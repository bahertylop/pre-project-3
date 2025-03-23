package org.bot.service.api;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.api.AuthFeignClient;
import org.bot.dto.SenderDto;
import org.bot.dto.TgUserDto;
import org.bot.model.TgUser;
import org.bot.repository.UserRepository;
import org.bot.service.UserService;
import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.request.TgAuthRequest;
import org.dto.response.JwtTokensResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;

    private final UserService userService;

    public boolean signInUser(Long chatId) {
        TgAuthRequest request = TgAuthRequest.builder().telegramId(chatId).build();

        try {
            JwtTokensResponse response = authFeignClient.signInUser(request);

            userService.setUserTokensBotAndStatus(response.getAccess(), response.getRefresh(), chatId, TgUser.BotState.WORKING);

            log.info("user chatId: {} signed in", chatId);
            return true;
        } catch (FeignException e) {
            log.warn("error with sign-in user chatId: {}", chatId, e);
            return false;
        }
    }

    public boolean refreshUserTokens(SenderDto sender) {
        try {
            JwtTokensResponse response = authFeignClient.refreshTokens(new RefreshTokenRequest(sender.getUser().getRefreshToken()));

            sender.setUser(userService.setUserTokens(response.getAccess(), response.getRefresh(), sender.getChatId()));
            return true;
        } catch (FeignException e) {
            log.error("tokens not refreshed", e);
            return false;
        }
    }
}
