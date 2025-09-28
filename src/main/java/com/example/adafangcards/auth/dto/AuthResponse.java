package com.example.adafangcards.auth.dto;
import java.util.Set;
import java.util.UUID;
import com.example.adafangcards.user.Role;
import com.example.adafangcards.user.dto.UserDto;


public record AuthResponse(String accessToken, UserDto user) {}
