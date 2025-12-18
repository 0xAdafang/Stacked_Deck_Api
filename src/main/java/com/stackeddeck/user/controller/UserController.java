package com.stackeddeck.user.controller;

import com.stackeddeck.security.SecurityUtils;
import com.stackeddeck.user.User;
import com.stackeddeck.user.UserService;
import com.stackeddeck.user.repo.UserRepository;
import com.stackeddeck.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/profile")
    public UserProfileDto getProfile() {
        return userService.getUserProfile(SecurityUtils.getCurrentUserId());
    }

    @PutMapping("/profile")
    public UserProfileDto updateProfile(@RequestBody UserProfileDto dto) {
        return userService.updateUserProfile(SecurityUtils.getCurrentUserId(), dto);
    }

}