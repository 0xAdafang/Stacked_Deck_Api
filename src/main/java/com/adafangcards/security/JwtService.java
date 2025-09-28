package com.adafangcards.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;


@Service
public class JwtService {
    private final byte[] key;
    private final long accessExpMin;
    public JwtService(@Value("${app.jwt.secret") String secret,
                      @Value("${app.jwt.access-exp-min") long accessExpMin) {
        this.key = secret.getBytes(StandardCharsets.UTF_8);
        this.accessExpMin = accessExpMin;
    }

    public String generate(Map<String,Object> claims, String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpMin * 60);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)   
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .parseClaimsJws(token);
    }
}
