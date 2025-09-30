package com.adafangcards.auth;

import com.adafangcards.auth.dto.*;
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
    @Value("${app.frontend.url}") String frontUrl;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        service.register(req);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/verify")
    public RedirectView verify(@RequestParam String token) {
        service.verify(token);
        return new RedirectView(frontUrl + "/login?verified");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }
}
