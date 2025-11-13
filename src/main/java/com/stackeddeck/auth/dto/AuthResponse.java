package com.stackeddeck.auth.dto;
import com.stackeddeck.user.dto.UserDto;


public record AuthResponse(String accessToken, UserDto user, Object refreshCookie) {}
