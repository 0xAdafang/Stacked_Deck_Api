package com.stackeddeck.auth.service;

import com.stackeddeck.auth.RefreshToken;
import com.stackeddeck.auth.dto.IssuedRefreshToken;
import com.stackeddeck.auth.repo.RefreshTokenRepository;
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

    public IssuedRefreshToken issue(UUID userId, int expDays) {
        var raw = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        var hash = encoder.encode(raw);

        var rt = RefreshToken.builder()
                .userId(userId)
                .tokenHash(hash)
                .expiresAt(Instant.now().plus(expDays, ChronoUnit.DAYS))
                .build();
        repo.save(rt);

        return new IssuedRefreshToken(raw, hash);
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
