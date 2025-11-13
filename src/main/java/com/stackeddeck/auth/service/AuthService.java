package com.stackeddeck.auth.service;

import com.stackeddeck.auth.VerificationToken;
import com.stackeddeck.auth.dto.*;
import com.stackeddeck.auth.repo.VerificationTokenRepository;
import com.stackeddeck.notifications.service.EmailService;
import com.stackeddeck.notifications.service.NotificationService;
import com.stackeddeck.security.service.JwtService;
import com.stackeddeck.user.*;
import com.stackeddeck.user.dto.*;
import com.stackeddeck.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final VerificationTokenRepository tokens;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final NotificationService notify;
    private final EmailService email;
    private final RefreshTokenService refreshTokens;


    @Value("${app.frontend.base-url}") String frontUrl;
    @Value("${app.refresh.exp-days}") int refreshExpDays;
    @Value("${app.refresh.cookie-name}") String refreshCookieName;

    @Transactional
    public void register(RegisterRequest req) {
        if (!req.password().equals(req.confirmPassword()))
            throw new IllegalArgumentException("Passwords don't match");
        if (users.existsByEmail(req.email())) throw new IllegalArgumentException("Email already exists");
        if (users.existsByUsername(req.username())) throw new IllegalArgumentException("Username already exists");


        var user = User.builder()
                .email(req.email())
                .username(req.username())
                .passwordHash(encoder.encode(req.password()))
                .roles(Set.of(Role.USER))
                .enabled(false)
                .build();
        users.save(user);

        var token = UUID.randomUUID().toString().replace("-", "");
        tokens.save(VerificationToken.builder()
                .token(token)
                .userId(user.getId())
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build());

        var link = frontUrl + "/auth/verify?token=" + token;
        email.send(user.getEmail(), "Verify your email - Stacked Deck",
                "Welcome " + user.getUsername() + "!\n\n Click to verify: " + link);



    }

    @Transactional
    public void verify(String token) {
        var vt = tokens.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    if (vt.getConsumedAt()!= null || vt.getExpiresAt().isBefore(Instant.now()))
        throw new IllegalArgumentException("Token invalid or expired");

    var user = users.findById(vt.getUserId()).orElseThrow();
        user.setEnabled(true);
        users.save(user);
        vt.setConsumedAt(Instant.now());
        tokens.save(vt);


    }

    public AuthResponse login(LoginRequest req) {
        var userOpt = req.identifier().contains("@")
            ? users.findByEmail(req.identifier())
            : users.findByUsername(req.identifier());
        var user = userOpt.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.isEnabled()) throw new IllegalStateException("Email not verified");
        if (!encoder.matches(req.password(), user.getPasswordHash()))
            throw new IllegalArgumentException("Invalid credentials");

        var claims = new HashMap<String,Object>();
        claims.put("roles", user.getRoles().stream().map(Enum::name).toArray(String[]::new));

        var accessToken = jwt.generateAccess(claims, user.getId().toString());

        var refreshToken = refreshTokens.issue(user.getId(), refreshExpDays);


        var dto = new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.isEnabled(), user.getRoles());
        return new AuthResponse(accessToken, dto, refreshToken.rawToken());
    }

    public String buildRefreshCookie(String raw, boolean clear) {
        var maxAge = clear ? 0 : refreshExpDays * 24 * 3600;

        return "%s=%s; Path=/api/auth; Max-Age=%d; HttpOnly; SameSite=Lax".formatted(
                refreshCookieName, clear ? "" : raw, maxAge);
    }

    public String buildRefreshSetCookie(String raw) { return buildRefreshCookie(raw, false); }
    public String buildRefreshClearCookie() { return buildRefreshCookie("", true); }

    public String issueRefreshForCurrentUser() {throw new UnsupportedOperationException();}

    @Transactional
    public RefreshResult refresh(String raw) {
        if (raw == null || raw.isBlank()) throw new IllegalArgumentException("No refresh token");
        var rt = refreshTokens.validationAndLoad(raw).orElseThrow(() -> new IllegalArgumentException("Invalid refresh"));

        var user = users.findById(rt.getUserId()).orElseThrow();
        if (!user.isEnabled()) throw new IllegalStateException("User disabled");


        refreshTokens.revoke(rt);
        var newRt = refreshTokens.issue(user.getId(), refreshExpDays);

        var claims = new HashMap<String,Object>();
        claims.put("roles", user.getRoles().stream().map(Enum::name).toArray(String[]::new));
        var access = jwt.generateAccess(claims, user.getId().toString());
        var dto = new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.isEnabled(), user.getRoles());
        return new RefreshResult(new AuthResponse(access, dto, newRt.rawToken()), newRt.rawToken());
    }

    @Transactional
    public void logout(String raw) {
        if (raw == null || raw.isBlank()) return;
        refreshTokens.validationAndLoad(raw).ifPresent(refreshTokens::revoke);
    }


    @Transactional
    public void resetPassword(String token, String newPassword) {
        var vt = tokens.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Token"));
        if (vt.getConsumedAt() != null || vt.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("Token already expired or used");

        var user = users.findById(vt.getUserId()).orElseThrow();
        user.setPasswordHash(encoder.encode(newPassword));
        users.save(user);

        vt.setConsumedAt(Instant.now());
        tokens.save(vt);
    }

    @Transactional
    public void resetPassword(String userEmail) {
        var user = users.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("No user with this email"));
        var token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        tokens.save(VerificationToken.builder()
                .token(token)
                .userId(user.getId())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build());
        var link = frontUrl + "/reset-password?token=" + token;
        email.send(user.getEmail(), "Reset your password - Stacked Deck",
                "Hello " + user.getUsername() + "!\n\n Click to reset your password: " + link);
    }




}



