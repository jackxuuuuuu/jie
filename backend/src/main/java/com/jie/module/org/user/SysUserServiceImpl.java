package com.jie.module.org.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.common.BizException;
import com.jie.common.PageResult;
import com.jie.module.org.dept.Dept;
import com.jie.module.org.dept.DeptMapper;
import com.jie.module.org.role.SysRole;
import com.jie.module.org.role.SysRoleMapper;
import com.jie.module.org.role.SysUserRole;
import com.jie.module.org.role.SysUserRoleMapper;
import com.jie.module.org.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final DeptMapper deptMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<UserDTO> listUsers(int pageNum, int pageSize, String keyword, Long deptId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(keyword), SysUser::getRealName, keyword)
                .or(StringUtils.hasText(keyword), w -> w.like(SysUser::getUsername, keyword))
                .eq(deptId != null, SysUser::getDeptId, deptId)
                .orderByDesc(SysUser::getCreatedAt);

        Page<SysUser> page = page(new Page<>(pageNum, pageSize), wrapper);
        List<UserDTO> dtos = page.getRecords().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return PageResult.of(page, dtos);
    }

    @Override
    public UserDTO getUserById(Long id) {
        SysUser user = getById(id);
        if (user == null) throw BizException.notFound("User");
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest req) {
        if (baseMapper.findByUsername(req.getUsername()) != null) {
            throw new BizException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setDeptId(req.getDeptId());
        user.setEmploymentStatus("IN_SERVICE");
        user.setEnabled(1);
        save(user);

        assignRole(user.getId(), req.getRoleCode());
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateRequest req) {
        SysUser user = getById(id);
        if (user == null) throw BizException.notFound("User");
        if (StringUtils.hasText(req.getRealName())) user.setRealName(req.getRealName());
        if (StringUtils.hasText(req.getPhone())) user.setPhone(req.getPhone());
        if (req.getDeptId() != null) user.setDeptId(req.getDeptId());
        updateById(user);

        if (StringUtils.hasText(req.getRoleCode())) {
            sysUserRoleMapper.deleteByUserId(id);
            assignRole(id, req.getRoleCode());
        }
        return toDTO(user);
    }

    @Override
    public void disableUser(Long id) {
        SysUser user = getById(id);
        if (user == null) throw BizException.notFound("User");
        user.setEnabled(0);
        user.setEmploymentStatus("LEFT");
        updateById(user);
    }

    @Override
    public void enableUser(Long id) {
        SysUser user = getById(id);
        if (user == null) throw BizException.notFound("User");
        user.setEnabled(1);
        user.setEmploymentStatus("IN_SERVICE");
        updateById(user);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        SysUser user = getById(id);
        if (user == null) throw BizException.notFound("User");
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    // ---- helpers ----

    private void assignRole(Long userId, String roleCode) {
        SysRole role = sysRoleMapper.selectOne(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, roleCode));
        if (role == null) throw new BizException("角色不存在: " + roleCode);
        SysUserRole ur = new SysUserRole();
        ur.setUserId(userId);
        ur.setRoleId(role.getId());
        sysUserRoleMapper.insert(ur);
    }

    private UserDTO toDTO(SysUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setPhone(user.getPhone());
        dto.setDeptId(user.getDeptId());
        dto.setEmploymentStatus(user.getEmploymentStatus());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        if (user.getDeptId() != null) {
            Dept dept = deptMapper.selectById(user.getDeptId());
            dto.setDeptName(dept != null ? dept.getName() : null);
        }
        dto.setRoleCodes(sysRoleMapper.findRoleCodesByUserId(user.getId()));
        return dto;
    }
}
