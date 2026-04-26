package com.jie.security;

import com.jie.module.org.user.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetails implementation that wraps SysUser.
 */
@Getter
public class LoginUser implements UserDetails {

    private final SysUser user;
    private final List<SimpleGrantedAuthority> authorities;

    public LoginUser(SysUser user, List<String> roleCodes) {
        this.user = user;
        this.authorities = roleCodes.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() != null && user.getEnabled() == 1;
    }
}
