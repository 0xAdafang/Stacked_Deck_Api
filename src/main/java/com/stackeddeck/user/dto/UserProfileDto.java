package com.stackeddeck.user.dto;

public record UserProfileDto(
        String email,
        String username,
        String firstName,
        String lastName,
        String phone,
        String addressLine1,
        String addressLine2,
        String city,
        String country,
        String postalCode
) {}