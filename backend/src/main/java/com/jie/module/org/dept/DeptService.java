package com.jie.module.org.dept;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeptService extends IService<Dept> {

    List<Dept> listAllActive();

    Dept createDept(DeptCreateRequest req);

    Dept updateDept(Long id, DeptCreateRequest req);

    void deleteDept(Long id);
}
