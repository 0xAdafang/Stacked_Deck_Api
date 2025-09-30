package com.adafangcards.security;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    @Test
    void generate_and_parse() {
        var svc = new JwtService("super-secret-1234567890super-secret", 15);
        var token = svc.generate(Map.of("roles", new String[]{"USER"}), "123");
        var claims = svc.parse(token).getBody();
        assertEquals("123", claims.getSubject());
    }
}
