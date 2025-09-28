package com.example.adafangcards.user.dto;


import java.util.Set;
import java.util.UUID;
import com.example.adafangcards.user.Role;

public record UserDto(UUID id, String email, String username, boolean enabled, Set<Role> roles) {}