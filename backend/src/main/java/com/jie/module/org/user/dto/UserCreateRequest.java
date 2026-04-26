package com.jie.module.org.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "创建用户请求")
public class UserCreateRequest {

    @NotBlank(message = "登录名不能为空")
    @Schema(description = "登录名/工号", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "初始密码", required = true)
    private String password;

    @NotBlank(message = "姓名不能为空")
    @Schema(description = "真实姓名", required = true)
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID", required = true)
    private Long deptId;

    @NotBlank(message = "角色不能为空")
    @Schema(description = "角色编码: ADMIN/HR/MANAGER/EMPLOYEE", required = true)
    private String roleCode;
}
