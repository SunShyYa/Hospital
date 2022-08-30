package com.sun.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.user.Patient;

import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 13:02
 **/
public interface PatientService extends IService<Patient> {
    List<Patient> findAllUserId(Long userId);
    Patient getPatientId(Long id);
}
