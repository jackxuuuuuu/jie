package com.jie.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "登录名/工号", required = true, example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true, example = "Jie@1234")
    private String password;
}
