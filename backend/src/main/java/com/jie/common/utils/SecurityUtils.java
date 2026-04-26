package com.jie.common.utils;

import com.jie.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility to access the current authenticated user.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static LoginUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser) {
            return (LoginUser) auth.getPrincipal();
        }
        return null;
    }

    public static Long currentUserId() {
        LoginUser u = currentUser();
        return u == null ? null : u.getUser().getId();
    }

    public static Long currentDeptId() {
        LoginUser u = currentUser();
        return u == null ? null : u.getUser().getDeptId();
    }

    public static boolean hasRole(String roleCode) {
        LoginUser u = currentUser();
        if (u == null) return false;
        return u.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + roleCode));
    }
}
