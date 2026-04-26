package com.jie.module.org.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "重置密码请求")
public class ResetPasswordRequest {

    @Schema(description = "新密码", required = true)
    private String newPassword;
}
