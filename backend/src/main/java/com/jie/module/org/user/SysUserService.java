package com.jie.module.org.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.common.PageResult;
import com.jie.module.org.user.dto.*;

public interface SysUserService extends IService<SysUser> {

    PageResult<UserDTO> listUsers(int pageNum, int pageSize, String keyword, Long deptId);

    UserDTO getUserById(Long id);

    UserDTO createUser(UserCreateRequest req);

    UserDTO updateUser(Long id, UserUpdateRequest req);

    void disableUser(Long id);

    void enableUser(Long id);

    void resetPassword(Long id, String newPassword);
}
