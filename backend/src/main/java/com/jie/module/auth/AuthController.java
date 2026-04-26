package com.jie.module.auth;

import com.jie.common.BizException;
import com.jie.common.Result;
import com.jie.module.auth.dto.LoginRequest;
import com.jie.module.auth.dto.LoginResponse;
import com.jie.module.org.user.SysUser;
import com.jie.module.org.user.SysUserMapper;
import com.jie.security.JwtUtils;
import com.jie.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Tag(name = "认证")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SysUserMapper sysUserMapper;

    @Operation(summary = "登录获取Token")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest req) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BizException(401, "用户名或密码错误");
        } catch (DisabledException e) {
            throw new BizException(401, "账号已被禁用");
        }

        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        SysUser user = loginUser.getUser();

        // update last login time
        user.setLastLoginAt(LocalDateTime.now());
        sysUserMapper.updateById(user);

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        java.util.List<String> roleCodes = loginUser.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());

        return Result.ok(new LoginResponse(token, user.getId(), user.getRealName(), roleCodes));
    }
}
