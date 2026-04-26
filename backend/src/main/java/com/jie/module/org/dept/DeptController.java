package com.jie.module.org.dept;

import com.jie.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/depts")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "部门列表（所有已认证用户可访问）")
    @GetMapping
    public Result<List<Dept>> list() {
        return Result.ok(deptService.listAllActive());
    }

    @Operation(summary = "新建部门（ADMIN）")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Dept> create(@Validated @RequestBody DeptCreateRequest req) {
        return Result.ok(deptService.createDept(req));
    }

    @Operation(summary = "更新部门（ADMIN）")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Dept> update(@PathVariable Long id,
                               @Validated @RequestBody DeptCreateRequest req) {
        return Result.ok(deptService.updateDept(id, req));
    }

    @Operation(summary = "删除部门（ADMIN）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.ok();
    }
}
