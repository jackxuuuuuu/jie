-- ============================================================
-- Seed data for Jie Leave Management System
-- Safe to re-run: uses INSERT IGNORE
-- ============================================================

-- Roles
INSERT IGNORE INTO sys_role (id, role_name, role_code) VALUES
(1, '管理员', 'ADMIN'),
(2, '人事HR', 'HR'),
(3, '部门主管', 'MANAGER'),
(4, '普通员工', 'EMPLOYEE');

-- Departments
INSERT IGNORE INTO dept (id, name, sort) VALUES
(1, '人事部',   1),
(2, '财务部',   2),
(3, '仓储部',   3),
(4, '配送部',   4),
(5, '客服部',   5);

-- Users (passwords are BCrypt of 'Jie@1234')
-- admin / Jie@1234
INSERT IGNORE INTO sys_user (id, username, password, real_name, phone, dept_id, employment_status, enabled) VALUES
(1, 'admin',    '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', '13800000001', NULL,       'IN_SERVICE', 1),
(2, 'hr01',     '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '人事王芳',   '13800000002', 1,          'IN_SERVICE', 1),
(3, 'mgr_ck',   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '仓储主管李磊','13800000003', 3,         'IN_SERVICE', 1),
(4, 'emp01',    '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '员工张三',   '13800000004', 3,          'IN_SERVICE', 1),
(5, 'emp02',    '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '员工李四',   '13800000005', 3,          'IN_SERVICE', 1);

-- Assign roles
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES
(1, 1),  -- admin -> ADMIN
(2, 2),  -- hr01 -> HR
(3, 3),  -- mgr_ck -> MANAGER
(4, 4),  -- emp01 -> EMPLOYEE
(5, 4);  -- emp02 -> EMPLOYEE

-- Set department leader (仓储部主管 = mgr_ck user_id=3)
UPDATE dept SET leader_user_id = 3 WHERE id = 3;
