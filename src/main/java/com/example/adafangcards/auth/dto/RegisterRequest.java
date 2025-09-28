package com.example.adafangcards.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest (

    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp="^[a-zA-Z0-9_\\\\-]{3,20}$") String username,
    @NotBlank @Size(min=8, max=100) String password,
    @NotBlank String confirmPassword
) {}
