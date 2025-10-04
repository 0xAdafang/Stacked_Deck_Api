package com.adafangmarket.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResult {
    private AuthResponse auth;
    private String refreshToken;
    private String refreshCookie;
    private String rawRefresh;

    public RefreshResult(AuthResponse auth, String rawRefresh) {
        this.auth = auth;
        this.rawRefresh = rawRefresh;
    }
    public AuthResponse auth() {
        return auth;
    }

    public String rawRefresh() {
        return rawRefresh;
    }


}