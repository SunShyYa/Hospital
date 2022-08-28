package com.sun.yygh.hosp.controller.api;

import com.sun.yygh.common.result.Result;
import com.sun.yygh.hosp.service.DepartmentService;
import com.sun.yygh.hosp.service.HospitalService;
import com.sun.yygh.model.hosp.Hospital;
import com.sun.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-27 14:33
 **/
@RestController
@RequestMapping("api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(@PathVariable String hosname) {
        List<Hospital> list = hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }

    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        return Result.ok(departmentService.findDeptTree(hoscode));
    }

    @GetMapping("findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode) {
        return Result.ok(hospitalService.item(hoscode));

    }
}
