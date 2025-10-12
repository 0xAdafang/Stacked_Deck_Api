package com.adafangmarket.security;

import com.adafangmarket.security.service.JwtService;
import com.adafangmarket.user.repo.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthFilter extends GenericFilter{

    private final JwtService jwt;
    private final UserRepository users;
    public JwtAuthFilter(JwtService jwt, UserRepository users) {this.jwt = jwt; this.users = users;}


    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ") && header.length() > 7) {
            String token = header.substring(7);
            try {
                Claims c = jwt.parse(token).getBody();
                var userId = c.getSubject();
                @SuppressWarnings("unchecked")
                List<String> roles = c.get("roles", List.class);
                var auths = roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toSet());

                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignored) {

            }
        }
        chain.doFilter(req, res);


    }
}
