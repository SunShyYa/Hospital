package com.sun.yygh.hosp.service;

import com.sun.yygh.model.hosp.Department;
import com.sun.yygh.vo.hosp.DepartmentQueryVo;
import com.sun.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 21:37
 **/
public interface DepartmentService {
    void save(Map<String, Object> map);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
