package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.dto.response.ProfileResponse;
import org.example.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
