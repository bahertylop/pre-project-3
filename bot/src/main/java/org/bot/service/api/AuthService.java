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
import org.dto.response.JwtTokensResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;

    private final UserService userService;

    public boolean signInUser(SenderDto sender, String password) {
        LoginRequest loginRequest = new LoginRequest(sender.getUser().getEmail(), password);

        try {
            JwtTokensResponse response = authFeignClient.signInUser(loginRequest);

            userService.setUserTokensBotAndStatus(response.getAccess(), response.getRefresh(), sender.getChatId(), TgUser.BotState.WORKING);

            log.info("user email: {} signed in", sender.getUser().getEmail());
            return true;
        } catch (FeignException e) {
            log.warn("error with sign-in user email: {}", sender.getUser().getEmail(), e);
            userService.changeUserBotStatus(sender, TgUser.BotState.EMAIL);
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
