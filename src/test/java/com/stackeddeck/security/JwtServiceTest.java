package com.stackeddeck.security;

import com.stackeddeck.security.service.JwtService;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    @Test
    void generate_and_parse() {
        var svc = new JwtService(
                "super-secret-1234567890super-secret",
                15,
                "test-issuer",
                "test-audience"
        );
        var token = svc.generateAccess(Map.of("roles", new String[]{"USER"}), "123");  // generateAccess au lieu de generate
        var claims = svc.parse(token).getBody();
        assertEquals("123", claims.getSubject());
        assertEquals("test-issuer", claims.getIssuer());
        assertEquals("test-audience", claims.getAudience());
    }
}