package com.jie.module.leave;

import com.jie.common.PageResult;
import com.jie.common.Result;
import com.jie.module.leave.dto.LeaveApproveRequest;
import com.jie.module.leave.dto.LeaveDTO;
import com.jie.module.leave.dto.LeaveSubmitRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "请假管理")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @Operation(summary = "提交请假（员工）")
    @PostMapping
    public Result<LeaveDTO> submit(@Validated @RequestBody LeaveSubmitRequest req) {
        return Result.ok(leaveRequestService.submitLeave(req));
    }

    @Operation(summary = "查看我的请假记录")
    @GetMapping("/mine")
    public Result<PageResult<LeaveDTO>> mine(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(leaveRequestService.listMyLeave(page, size));
    }

    @Operation(summary = "请假列表（HR/ADMIN全量；MANAGER仅本部门）")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public Result<PageResult<LeaveDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long deptId) {
        return Result.ok(leaveRequestService.listLeave(page, size, status, deptId));
    }

    @Operation(summary = "请假详情")
    @GetMapping("/{id}")
    public Result<LeaveDTO> get(@PathVariable Long id) {
        return Result.ok(leaveRequestService.getLeave(id));
    }

    @Operation(summary = "审批通过（MANAGER/ADMIN/HR）")
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public Result<LeaveDTO> approve(@PathVariable Long id,
                                    @RequestBody(required = false) LeaveApproveRequest req) {
        return Result.ok(leaveRequestService.approve(id, req != null ? req : new LeaveApproveRequest()));
    }

    @Operation(summary = "拒绝请假（MANAGER/ADMIN/HR）")
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public Result<LeaveDTO> reject(@PathVariable Long id,
                                   @RequestBody(required = false) LeaveApproveRequest req) {
        return Result.ok(leaveRequestService.reject(id, req != null ? req : new LeaveApproveRequest()));
    }

    @Operation(summary = "撤回请假（员工本人）")
    @PostMapping("/{id}/cancel")
    public Result<LeaveDTO> cancel(@PathVariable Long id) {
        return Result.ok(leaveRequestService.cancel(id));
    }
}
