package com.adafangmarket.auth.controller;

import com.adafangmarket.auth.dto.*;
import com.adafangmarket.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    @Value("${app.frontend.base-url}") String frontUrl;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        service.register(req);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/verify")
    public RedirectView verify(@RequestParam String token) {
        service.verify(token);
        return new RedirectView(frontUrl + "/auth/verify-email?token=" + token);
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "${app.refresh.cookie-name}", required = false) String raw) {
        var resp = service.refresh(raw);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, service.buildRefreshSetCookie(resp.rawRefresh()))
                .body(resp.auth());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = service.login(req);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, service.buildRefreshSetCookie(result.refreshCookie().toString()))
                .body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "${app.refresh.cookie-name}", required = false) String raw) {
        service.logout(raw);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, service.buildRefreshClearCookie())
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgot(@RequestParam String email) { service.resetPassword(email); return ResponseEntity.accepted().build(); }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> reset(@RequestParam String token, @RequestParam String newPassword) { service.resetPassword(token, newPassword); return ResponseEntity.noContent().build(); }

}
