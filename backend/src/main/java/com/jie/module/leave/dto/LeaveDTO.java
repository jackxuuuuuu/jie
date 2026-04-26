package com.jie.module.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "请假申请详情DTO")
public class LeaveDTO {

    @Schema(description = "申请ID")
    private Long id;

    @Schema(description = "申请人用户ID")
    private Long applicantUserId;

    @Schema(description = "申请人姓名")
    private String applicantName;

    @Schema(description = "申请人部门ID")
    private Long applicantDeptId;

    @Schema(description = "申请人部门名称")
    private String applicantDeptName;

    @Schema(description = "请假类型")
    private String leaveType;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "时长（小时）")
    private BigDecimal durationHours;

    @Schema(description = "原因")
    private String reason;

    @Schema(description = "状态: SUBMITTED/APPROVED/REJECTED/CANCELED")
    private String status;

    @Schema(description = "审批人ID")
    private Long approverUserId;

    @Schema(description = "审批人姓名")
    private String approverName;

    @Schema(description = "审批时间")
    private LocalDateTime approveTime;

    @Schema(description = "审批意见")
    private String approveComment;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
