package com.jie.common.enums;

/**
 * Leave/overtime request status flow.
 */
public enum LeaveStatus {
    SUBMITTED,  // 已提交（待审批）
    APPROVED,   // 已通过
    REJECTED,   // 已拒绝
    CANCELED    // 已撤回
}
