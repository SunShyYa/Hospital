package com.sun.yygh.user.api;

import com.sun.yygh.common.result.Result;
import com.sun.yygh.common.utils.AuthContextHolder;
import com.sun.yygh.model.user.Patient;
import com.sun.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 13:04
 **/
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {
    @Autowired
    private PatientService patientService;

    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
    //修改就诊人
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }
    //删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable Long id) {
        return patientService.getById(id);
    }
}
