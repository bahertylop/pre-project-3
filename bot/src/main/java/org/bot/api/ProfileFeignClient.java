package org.bot.api;

import org.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "profileClient", url = "${api.url.base-url}")
public interface ProfileFeignClient {

    @GetMapping(value = "${api.url.get-profile}")
    ProfileResponse getProfileInfo(@RequestHeader(name = "Authorization") String accessToken);
}
