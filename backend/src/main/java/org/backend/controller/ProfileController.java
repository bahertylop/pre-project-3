package org.backend.controller;

import lombok.RequiredArgsConstructor;

import org.backend.dto.request.UpdateProfileRequest;
import org.backend.dto.response.ProfileResponse;
import org.backend.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileResponse getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return profileService.getUserInfo(userDetails);
    }

    @DeleteMapping
    public void deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteProfile(userDetails);
    }

    @PutMapping
    public void updateUserInfo(@RequestBody @Validated UpdateProfileRequest updateInfo,
                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateUserInfo(userDetails, updateInfo);
    }
}
