package com.stackeddeck.auth.dto;

public record IssuedRefreshToken(String rawToken, String tokenHash) {}