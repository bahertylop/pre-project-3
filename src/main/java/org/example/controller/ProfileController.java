package org.example.controller;

import lombok.RequiredArgsConstructor;

import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.response.ProfileResponse;
import org.example.service.ProfileService;
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

    @PostMapping("/delete")
    public void deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteProfile(userDetails);
    }

    @PostMapping("/update")
    public void updateUserInfo(@RequestBody @Validated UpdateProfileRequest updateInfo,
                               @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateUserInfo(userDetails, updateInfo);
    }
}
