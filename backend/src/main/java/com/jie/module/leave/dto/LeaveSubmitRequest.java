package com.jie.module.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "提交请假申请")
public class LeaveSubmitRequest {

    @NotBlank(message = "请假类型不能为空")
    @Schema(description = "请假类型: SICK/THING/ANNUAL/OTHER", required = true)
    private String leaveType;

    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间", required = true)
    private LocalDateTime endTime;

    @Schema(description = "请假原因")
    private String reason;
}
