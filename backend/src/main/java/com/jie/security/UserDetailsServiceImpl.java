package com.jie.security;

import com.jie.module.org.role.SysRoleMapper;
import com.jie.module.org.user.SysUser;
import com.jie.module.org.user.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<String> roleCodes = sysRoleMapper.findRoleCodesByUserId(user.getId());
        return new LoginUser(user, roleCodes);
    }

    public UserDetails loadUserByUserId(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: id=" + userId);
        }
        List<String> roleCodes = sysRoleMapper.findRoleCodesByUserId(user.getId());
        return new LoginUser(user, roleCodes);
    }
}
