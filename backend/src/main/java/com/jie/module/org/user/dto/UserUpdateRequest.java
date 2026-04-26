package com.jie.module.org.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新用户请求")
public class UserUpdateRequest {

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "角色编码: ADMIN/HR/MANAGER/EMPLOYEE")
    private String roleCode;
}
