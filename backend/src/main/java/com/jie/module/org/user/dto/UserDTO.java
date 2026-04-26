package com.jie.module.org.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "用户信息DTO（不含密码）")
public class UserDTO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "登录名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "在职状态")
    private String employmentStatus;

    @Schema(description = "账号是否启用")
    private Integer enabled;

    @Schema(description = "角色编码列表")
    private List<String> roleCodes;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
