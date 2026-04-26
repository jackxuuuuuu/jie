package com.jie.module.leave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.common.PageResult;
import com.jie.module.leave.dto.LeaveApproveRequest;
import com.jie.module.leave.dto.LeaveDTO;
import com.jie.module.leave.dto.LeaveSubmitRequest;

public interface LeaveRequestService extends IService<LeaveRequest> {

    LeaveDTO submitLeave(LeaveSubmitRequest req);

    PageResult<LeaveDTO> listMyLeave(int pageNum, int pageSize);

    PageResult<LeaveDTO> listLeave(int pageNum, int pageSize, String status, Long deptId);

    LeaveDTO getLeave(Long id);

    LeaveDTO approve(Long id, LeaveApproveRequest req);

    LeaveDTO reject(Long id, LeaveApproveRequest req);

    LeaveDTO cancel(Long id);
}
