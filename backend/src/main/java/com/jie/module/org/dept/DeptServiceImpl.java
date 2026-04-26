package com.jie.module.org.dept;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.common.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public List<Dept> listAllActive() {
        return list(new LambdaQueryWrapper<Dept>()
                .eq(Dept::getStatus, 1)
                .orderByAsc(Dept::getSort));
    }

    @Override
    public Dept createDept(DeptCreateRequest req) {
        Dept dept = new Dept();
        dept.setName(req.getName());
        dept.setLeaderUserId(req.getLeaderUserId());
        dept.setSort(req.getSort() != null ? req.getSort() : 0);
        dept.setStatus(1);
        save(dept);
        return dept;
    }

    @Override
    public Dept updateDept(Long id, DeptCreateRequest req) {
        Dept dept = getById(id);
        if (dept == null) throw BizException.notFound("Dept");
        if (req.getName() != null) dept.setName(req.getName());
        if (req.getLeaderUserId() != null) dept.setLeaderUserId(req.getLeaderUserId());
        if (req.getSort() != null) dept.setSort(req.getSort());
        updateById(dept);
        return dept;
    }

    @Override
    public void deleteDept(Long id) {
        Dept dept = getById(id);
        if (dept == null) throw BizException.notFound("Dept");
        removeById(id);
    }
}
