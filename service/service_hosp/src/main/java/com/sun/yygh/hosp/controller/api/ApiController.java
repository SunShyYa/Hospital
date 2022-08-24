package com.sun.yygh.hosp.controller.api;

import com.alibaba.excel.util.StringUtils;
import com.sun.yygh.common.exception.YyghException;
import com.sun.yygh.common.helper.HttpRequestHelper;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.common.result.ResultCodeEnum;
import com.sun.yygh.common.utils.MD5;
import com.sun.yygh.hosp.service.DepartmentService;
import com.sun.yygh.hosp.service.HospitalService;
import com.sun.yygh.hosp.service.HospitalSetService;
import com.sun.yygh.hosp.service.ScheduleService;
import com.sun.yygh.model.hosp.Department;
import com.sun.yygh.model.hosp.Hospital;
import com.sun.yygh.model.hosp.Schedule;
import com.sun.yygh.vo.hosp.DepartmentQueryVo;

import com.sun.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 19:20
 **/
@RestController
@RequestMapping("api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, Object> map = getStringObjectMap(request);

        hospitalService.save(map);
        return Result.ok();
    }

    private Map<String, Object> getStringObjectMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return map;
    }

    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        String hoscode = getHoscode(request);
        Hospital byHoscode = hospitalService.getByHoscode(hoscode);
        return Result.ok(byHoscode);
    }

    private String getHoscode(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return hoscode;
    }

    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> map = getStringObjectMap(request);
        departmentService.save(map);
        return Result.ok();
    }

    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        int page = StringUtils.isEmpty(map.get("page"))? 1 : Integer.parseInt((String)map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit"))? 1 : Integer.parseInt((String)map.get("limit"));
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel =  departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String depcode = (String)map.get("depcode");
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @PostMapping("schedule/list")
    public Result  findSchedule(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String hosScheduled = (String) map.get("hosScheduledId");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        int page = StringUtils.isEmpty(map.get("page"))? 1 : Integer.parseInt((String)map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit"))? 1 : Integer.parseInt((String)map.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> map = getStringObjectMap(request);
        scheduleService.save(map);
        return Result.ok();
    }

    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String encrypt = MD5.encrypt(signKey);
        if (!sign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String scheduleId = (String)map.get("hosScheduleId");
        scheduleService.remove(hoscode, scheduleId);
        return Result.ok();
    }



}
