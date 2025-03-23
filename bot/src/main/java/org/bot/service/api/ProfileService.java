package org.bot.service.api;

import lombok.RequiredArgsConstructor;
import org.bot.api.ProfileFeignClient;
import org.bot.dto.SenderDto;
import org.bot.util.JwtTokenUtil;
import org.dto.response.ProfileResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final RetryService retryService;

    private final ProfileFeignClient profileFeignClient;

    public Optional<ProfileResponse> getProfileInfo(SenderDto senderDto) {
        return Optional.ofNullable(retryService.executeWithRetry(
                () -> profileFeignClient.getProfileInfo(JwtTokenUtil.bearerToken(senderDto.getUser().getJwtToken())),
                senderDto,
                null
        ));
    }
}
