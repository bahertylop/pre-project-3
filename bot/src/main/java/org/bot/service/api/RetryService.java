package org.bot.service.api;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.dto.SenderDto;
import org.bot.service.api.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryService {

    private static final int MAX_RETRY_COUNT = 2;

    private final AuthService authService;

    public <T> T executeWithRetry(Supplier<T> request, SenderDto sender, T defaultValue) {
        for (int attempt = 1; attempt <= MAX_RETRY_COUNT; attempt++) {
            try {
                return request.get();
            } catch (FeignException e) {
                if (e.status() == HttpStatus.FORBIDDEN.value()) {
                    log.warn("forbidden for request chatId: {}", sender.getChatId(), e);
                    if (attempt == MAX_RETRY_COUNT || !authService.refreshUserTokens(sender)) {
                        log.error("failed to refresh tokens chatId: {}", sender.getChatId());
                        return defaultValue;
                    }
                } else {
                    log.warn("request failed chatId: {}", sender.getChatId(), e);
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }
}
