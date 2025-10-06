package com.adafangmarket.auth.dto;

public record IssuedRefreshToken(String rawToken, String tokenHash) {}