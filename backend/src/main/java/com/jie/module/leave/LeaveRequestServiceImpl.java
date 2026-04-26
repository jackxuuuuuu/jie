package com.jie.module.leave;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.common.BizException;
import com.jie.common.PageResult;
import com.jie.common.utils.SecurityUtils;
import com.jie.module.leave.dto.LeaveApproveRequest;
import com.jie.module.leave.dto.LeaveDTO;
import com.jie.module.leave.dto.LeaveSubmitRequest;
import com.jie.module.org.dept.Dept;
import com.jie.module.org.dept.DeptMapper;
import com.jie.module.org.user.SysUser;
import com.jie.module.org.user.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest>
        implements LeaveRequestService {

    private final SysUserMapper sysUserMapper;
    private final DeptMapper deptMapper;

    @Override
    @Transactional
    public LeaveDTO submitLeave(LeaveSubmitRequest req) {
        Long currentUserId = SecurityUtils.currentUserId();
        Long currentDeptId = SecurityUtils.currentDeptId();

        if (req.getEndTime().isBefore(req.getStartTime()) || req.getEndTime().isEqual(req.getStartTime())) {
            throw new BizException("结束时间必须晚于开始时间");
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setApplicantUserId(currentUserId);
        leave.setApplicantDeptId(currentDeptId);
        leave.setLeaveType(req.getLeaveType());
        leave.setStartTime(req.getStartTime());
        leave.setEndTime(req.getEndTime());
        leave.setReason(req.getReason());
        leave.setStatus("SUBMITTED");
        save(leave);
        return toDTO(leave);
    }

    @Override
    public PageResult<LeaveDTO> listMyLeave(int pageNum, int pageSize) {
        Long currentUserId = SecurityUtils.currentUserId();
        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<LeaveRequest>()
                .eq(LeaveRequest::getApplicantUserId, currentUserId)
                .orderByDesc(LeaveRequest::getCreatedAt);
        Page<LeaveRequest> page = page(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public PageResult<LeaveDTO> listLeave(int pageNum, int pageSize, String status, Long deptId) {
        // Data-scope enforcement:
        // ADMIN/HR -> full access (filtered by deptId/status if provided)
        // MANAGER  -> only own dept
        // EMPLOYEE -> redirected to listMyLeave (controller handles)
        Long scopeDeptId = deptId;
        if (SecurityUtils.hasRole("MANAGER") && !SecurityUtils.hasRole("ADMIN") && !SecurityUtils.hasRole("HR")) {
            scopeDeptId = SecurityUtils.currentDeptId();
        }

        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<LeaveRequest>()
                .eq(StringUtils.hasText(status), LeaveRequest::getStatus, status)
                .eq(scopeDeptId != null, LeaveRequest::getApplicantDeptId, scopeDeptId)
                .orderByDesc(LeaveRequest::getCreatedAt);
        Page<LeaveRequest> page = page(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public LeaveDTO getLeave(Long id) {
        LeaveRequest leave = getById(id);
        if (leave == null) throw BizException.notFound("LeaveRequest");
        checkAccess(leave);
        return toDTO(leave);
    }

    @Override
    @Transactional
    public LeaveDTO approve(Long id, LeaveApproveRequest req) {
        LeaveRequest leave = getAndCheckForApproval(id);
        leave.setStatus("APPROVED");
        leave.setApproverUserId(SecurityUtils.currentUserId());
        leave.setApproveTime(LocalDateTime.now());
        leave.setApproveComment(req.getComment());
        // fix duration in hours
        long minutes = ChronoUnit.MINUTES.between(leave.getStartTime(), leave.getEndTime());
        leave.setDurationHours(BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_UP));
        updateById(leave);
        return toDTO(leave);
    }

    @Override
    @Transactional
    public LeaveDTO reject(Long id, LeaveApproveRequest req) {
        LeaveRequest leave = getAndCheckForApproval(id);
        leave.setStatus("REJECTED");
        leave.setApproverUserId(SecurityUtils.currentUserId());
        leave.setApproveTime(LocalDateTime.now());
        leave.setApproveComment(req.getComment());
        updateById(leave);
        return toDTO(leave);
    }

    @Override
    @Transactional
    public LeaveDTO cancel(Long id) {
        LeaveRequest leave = getById(id);
        if (leave == null) throw BizException.notFound("LeaveRequest");
        if (!leave.getApplicantUserId().equals(SecurityUtils.currentUserId())) {
            throw BizException.forbidden("只能撤回自己的请假");
        }
        if (!"SUBMITTED".equals(leave.getStatus())) {
            throw new BizException("只有待审批的请假可以撤回");
        }
        leave.setStatus("CANCELED");
        updateById(leave);
        return toDTO(leave);
    }

    // ---- helpers ----

    private LeaveRequest getAndCheckForApproval(Long id) {
        LeaveRequest leave = getById(id);
        if (leave == null) throw BizException.notFound("LeaveRequest");
        if (!"SUBMITTED".equals(leave.getStatus())) {
            throw new BizException("该申请当前状态不允许审批操作");
        }
        // MANAGER can only approve leaves in own dept
        if (SecurityUtils.hasRole("MANAGER") && !SecurityUtils.hasRole("ADMIN") && !SecurityUtils.hasRole("HR")) {
            if (!leave.getApplicantDeptId().equals(SecurityUtils.currentDeptId())) {
                throw BizException.forbidden("只能审批本部门的请假");
            }
        }
        return leave;
    }

    private void checkAccess(LeaveRequest leave) {
        Long currentUserId = SecurityUtils.currentUserId();
        boolean isAdminOrHr = SecurityUtils.hasRole("ADMIN") || SecurityUtils.hasRole("HR");
        boolean isManager = SecurityUtils.hasRole("MANAGER");
        if (isAdminOrHr) return;
        if (isManager && leave.getApplicantDeptId().equals(SecurityUtils.currentDeptId())) return;
        if (leave.getApplicantUserId().equals(currentUserId)) return;
        throw BizException.forbidden("无权访问该请假记录");
    }

    private PageResult<LeaveDTO> toPageResult(Page<LeaveRequest> page) {
        List<LeaveDTO> dtos = page.getRecords().stream().map(this::toDTO).collect(Collectors.toList());
        return PageResult.of(page, dtos);
    }

    private LeaveDTO toDTO(LeaveRequest leave) {
        LeaveDTO dto = new LeaveDTO();
        dto.setId(leave.getId());
        dto.setApplicantUserId(leave.getApplicantUserId());
        dto.setApplicantDeptId(leave.getApplicantDeptId());
        dto.setLeaveType(leave.getLeaveType());
        dto.setStartTime(leave.getStartTime());
        dto.setEndTime(leave.getEndTime());
        dto.setDurationHours(leave.getDurationHours());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        dto.setApproverUserId(leave.getApproverUserId());
        dto.setApproveTime(leave.getApproveTime());
        dto.setApproveComment(leave.getApproveComment());
        dto.setCreatedAt(leave.getCreatedAt());

        SysUser applicant = sysUserMapper.selectById(leave.getApplicantUserId());
        if (applicant != null) dto.setApplicantName(applicant.getRealName());

        Dept dept = leave.getApplicantDeptId() != null ? deptMapper.selectById(leave.getApplicantDeptId()) : null;
        if (dept != null) dto.setApplicantDeptName(dept.getName());

        if (leave.getApproverUserId() != null) {
            SysUser approver = sysUserMapper.selectById(leave.getApproverUserId());
            if (approver != null) dto.setApproverName(approver.getRealName());
        }
        return dto;
    }
}
