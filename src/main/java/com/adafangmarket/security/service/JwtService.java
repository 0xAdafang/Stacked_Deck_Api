package com.adafangmarket.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Service
public class JwtService {
    private final byte[] key;
    private final long accessExpMin;
    private final String issuer;
    private final String audience;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-exp-min}") long accessExpMin,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.audience}") String audience
    ) {
        this.key = secret.getBytes(StandardCharsets.UTF_8);
        this.accessExpMin = accessExpMin;
        this.issuer = issuer;
        this.audience = audience;
    }



    public String generateAccess(Map<String, Object> claims, String userId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpMin);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .parseClaimsJws(token);
    }
}
