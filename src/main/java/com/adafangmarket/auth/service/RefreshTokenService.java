package com.adafangmarket.auth.service;

import com.adafangmarket.auth.RefreshToken;
import com.adafangmarket.auth.repo.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final PasswordEncoder encoder;

    public RefreshToken issue(UUID userId, int expDays) {
        var raw = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID();
        var rt = RefreshToken.builder()
                .userId(userId)
                .tokenHash(encoder.encode(raw))
                .expiresAt(Instant.now().plus(expDays, ChronoUnit.DAYS))
                .build();
        repo.save(rt);
        rt.setTokenHash(raw);
        return rt;
    }

    public Optional<RefreshToken> validationAndLoad(String raw) {
        return repo.findAll().stream()
                .filter(rt -> rt.getRevokedAt() == null && rt.getExpiresAt().isAfter(Instant.now()) && encoder.matches(raw, rt.getTokenHash()))
                .findFirst();
    }

    public void revoke(RefreshToken rt) {
        rt.setRevokedAt(Instant.now());
        repo.save(rt);
    }
}
