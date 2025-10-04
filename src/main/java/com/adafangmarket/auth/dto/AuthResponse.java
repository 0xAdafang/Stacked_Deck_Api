package com.adafangmarket.auth.dto;
import com.adafangmarket.user.dto.UserDto;


public record AuthResponse(String accessToken, UserDto user, Object refreshCookie) {}
