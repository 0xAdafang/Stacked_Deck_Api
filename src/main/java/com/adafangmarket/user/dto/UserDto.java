package com.adafangmarket.user.dto;


import java.util.Set;
import java.util.UUID;
import com.adafangmarket.user.Role;

public record UserDto(
        UUID id,
        String email,
        String username,
        boolean enabled,
        Set<Role> roles
) {}