package com.ead.course.utils;

import com.ead.course.configs.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility methods to access the authenticated user from the SecurityContext.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Returns the authenticated UserDetailsImpl if present.
     */
    public static Optional<UserDetailsImpl> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return Optional.of((UserDetailsImpl) principal);
        }
        return Optional.empty();
    }

    /**
     * Returns the authenticated user id if present.
     */
    public static Optional<UUID> getAuthenticatedUserId() {
        return getAuthenticatedUser().map(UserDetailsImpl::getUserId);
    }

    /**
     * Convenience method to check if the authenticated user has ROLE_ADMIN.
     */
    public static boolean isAdmin() {
        return getAuthenticatedUser().map(u -> u.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).orElse(false);
    }
}

