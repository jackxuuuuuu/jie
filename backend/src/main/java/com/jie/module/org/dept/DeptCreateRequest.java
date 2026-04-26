package com.jie.module.org.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "创建/更新部门请求")
public class DeptCreateRequest {

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称", required = true)
    private String name;

    @Schema(description = "部门主管用户ID")
    private Long leaderUserId;

    @Schema(description = "排序", example = "1")
    private Integer sort;
}
