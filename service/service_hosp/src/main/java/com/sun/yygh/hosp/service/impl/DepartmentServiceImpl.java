package com.sun.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.sun.yygh.hosp.repository.DepartmentRepository;
import com.sun.yygh.hosp.service.DepartmentService;
import com.sun.yygh.model.hosp.Department;
import com.sun.yygh.vo.hosp.DepartmentQueryVo;
import com.sun.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 21:38
 **/
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return departmentByHoscodeAndDepcode.getDepname();

    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> result = new ArrayList<>();
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> of = Example.of(department);
        List<Department> all = departmentRepository.findAll(of);

        Map<String, List<Department>> collect =
                all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for(Map.Entry<String,List<Department>> entry : collect.entrySet()) {
            String bigcode = entry.getKey();
            List<Department> value = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(value.get(0).getBigname());
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department1 : value) {
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepcode(department1.getDepcode());
                departmentVo1.setDepname(department1.getDepname());
                children.add(departmentVo1);
            }
            departmentVo.setChildren(children);
            result.add(departmentVo);
        }
        return result;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (departmentByHoscodeAndDepcode != null) {
            departmentRepository.deleteById(departmentByHoscodeAndDepcode.getId());
        }
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void save(Map<String, Object> map) {

        Department department = JSONObject.parseObject(JSONObject.toJSONString(map), Department.class);
        Department d = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),
                department.getDepcode());
        if (d != null) {
            d.setUpdateTime(new Date());
            d.setIsDeleted(0);
            departmentRepository.save(d);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
    }
}
