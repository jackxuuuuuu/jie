package com.jie.module.leave;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("leave_request")
public class LeaveRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applicantUserId;

    private Long applicantDeptId;

    private String leaveType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal durationHours;

    private String reason;

    private String status;

    private Long approverUserId;

    private LocalDateTime approveTime;

    private String approveComment;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
