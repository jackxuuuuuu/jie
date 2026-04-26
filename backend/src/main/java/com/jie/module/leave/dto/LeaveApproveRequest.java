package com.jie.module.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "审批请假申请")
public class LeaveApproveRequest {

    @Schema(description = "审批意见")
    private String comment;
}
