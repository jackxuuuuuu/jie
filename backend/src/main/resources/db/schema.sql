-- ============================================================
-- Schema for Jie Leave Management System
-- Idempotent: uses CREATE TABLE IF NOT EXISTS
-- ============================================================

CREATE TABLE IF NOT EXISTS dept (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    name        VARCHAR(64)  NOT NULL COMMENT '部门名称',
    leader_user_id BIGINT    DEFAULT NULL COMMENT '部门主管用户ID',
    sort        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(32)  NOT NULL COMMENT '角色编码 ADMIN/HR/MANAGER/EMPLOYEE',
    deleted     TINYINT      NOT NULL DEFAULT 0,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS sys_user (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username        VARCHAR(64)  NOT NULL COMMENT '登录名/工号',
    password        VARCHAR(128) NOT NULL COMMENT '密码(BCrypt)',
    real_name       VARCHAR(64)  NOT NULL COMMENT '真实姓名',
    phone           VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    dept_id         BIGINT       DEFAULT NULL COMMENT '部门ID',
    employment_status VARCHAR(16) NOT NULL DEFAULT 'IN_SERVICE' COMMENT 'IN_SERVICE/LEFT',
    enabled         TINYINT      NOT NULL DEFAULT 1 COMMENT '账号是否启用',
    deleted         TINYINT      NOT NULL DEFAULT 0,
    last_login_at   DATETIME     DEFAULT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE IF NOT EXISTS sys_user_role (
    id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS leave_request (
    id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '请假单ID',
    applicant_user_id   BIGINT       NOT NULL COMMENT '申请人用户ID',
    applicant_dept_id   BIGINT       NOT NULL COMMENT '申请时部门ID（历史快照）',
    leave_type          VARCHAR(16)  NOT NULL COMMENT 'SICK/THING/ANNUAL/OTHER',
    start_time          DATETIME     NOT NULL COMMENT '开始时间',
    end_time            DATETIME     NOT NULL COMMENT '结束时间',
    duration_hours      DECIMAL(6,1) DEFAULT NULL COMMENT '时长（小时，审批通过后固化）',
    reason              VARCHAR(512) DEFAULT NULL COMMENT '请假原因',
    status              VARCHAR(16)  NOT NULL DEFAULT 'SUBMITTED' COMMENT 'SUBMITTED/APPROVED/REJECTED/CANCELED',
    approver_user_id    BIGINT       DEFAULT NULL COMMENT '审批人用户ID',
    approve_time        DATETIME     DEFAULT NULL COMMENT '审批时间',
    approve_comment     VARCHAR(256) DEFAULT NULL COMMENT '审批意见',
    deleted             TINYINT      NOT NULL DEFAULT 0,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_applicant (applicant_user_id),
    KEY idx_dept (applicant_dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

CREATE TABLE IF NOT EXISTS audit_log (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    biz_type         VARCHAR(32)  NOT NULL COMMENT '业务类型 LEAVE/USER',
    biz_id           BIGINT       NOT NULL COMMENT '业务ID',
    action           VARCHAR(32)  NOT NULL COMMENT 'SUBMIT/APPROVE/REJECT/CANCEL/CREATE/UPDATE/DISABLE',
    operator_user_id BIGINT       NOT NULL COMMENT '操作人',
    comment          VARCHAR(256) DEFAULT NULL COMMENT '备注',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_biz (biz_type, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';
