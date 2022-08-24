package com.sun.yygh.hosp.service;

import com.sun.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 19:16
 **/
public interface HospitalService {
    void save(Map<String,Object> paramMap);

    Hospital getByHoscode(String hoscode);
}
