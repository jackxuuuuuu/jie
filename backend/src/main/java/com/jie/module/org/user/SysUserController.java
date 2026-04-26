package com.jie.module.org.user;

import com.jie.common.BizException;
import com.jie.common.PageResult;
import com.jie.common.Result;
import com.jie.common.utils.SecurityUtils;
import com.jie.module.org.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@Tag(name = "用户管理")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "用户列表（HR/ADMIN全量；MANAGER仅本部门）")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public Result<PageResult<UserDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long deptId) {

        // MANAGER can only see their own department
        if (SecurityUtils.hasRole("MANAGER") && !SecurityUtils.hasRole("ADMIN") && !SecurityUtils.hasRole("HR")) {
            Long myDeptId = SecurityUtils.currentDeptId();
            if (deptId != null && !deptId.equals(myDeptId)) {
                throw BizException.forbidden("Manager can only view own department");
            }
            deptId = myDeptId;
        }
        return Result.ok(userService.listUsers(page, size, keyword, deptId));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER') or #id == authentication.principal.user.id")
    public Result<UserDTO> get(@PathVariable Long id) {
        return Result.ok(userService.getUserById(id));
    }

    @Operation(summary = "新建用户（ADMIN/HR）")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<UserDTO> create(@Validated @RequestBody UserCreateRequest req) {
        return Result.ok(userService.createUser(req));
    }

    @Operation(summary = "更新用户（ADMIN/HR）")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<UserDTO> update(@PathVariable Long id,
                                  @RequestBody UserUpdateRequest req) {
        return Result.ok(userService.updateUser(id, req));
    }

    @Operation(summary = "停用/离职用户（ADMIN/HR）")
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> disable(@PathVariable Long id) {
        userService.disableUser(id);
        return Result.ok();
    }

    @Operation(summary = "启用用户（ADMIN/HR）")
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> enable(@PathVariable Long id) {
        userService.enableUser(id);
        return Result.ok();
    }

    @Operation(summary = "重置密码（ADMIN/HR）")
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public Result<Void> resetPassword(@PathVariable Long id,
                                      @Validated @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(id, req.getNewPassword());
        return Result.ok();
    }
}
