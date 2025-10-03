package com.adafangcards.auth.dto;
import com.adafangcards.user.dto.UserDto;


public record AuthResponse(String accessToken, UserDto user, Object refreshCookie) {}
