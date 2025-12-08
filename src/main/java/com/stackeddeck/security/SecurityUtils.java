package com.stackeddeck.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        try {
            Object principal = auth.getPrincipal();

            if (principal instanceof String) {
                return UUID.fromString((String) principal);
            } else if (principal instanceof UserDetails) {
                return UUID.fromString(((UserDetails) principal).getUsername());
            } else if (principal instanceof java.security.Principal) {
                return UUID.fromString(((java.security.Principal) principal).getName());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User ID in token");
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User ID in token");
        }
    }
}
