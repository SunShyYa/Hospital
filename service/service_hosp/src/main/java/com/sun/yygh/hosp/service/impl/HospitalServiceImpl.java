package com.sun.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.yygh.hosp.repository.HospitalRepository;
import com.sun.yygh.hosp.service.HospitalService;
import com.sun.yygh.model.hosp.Hospital;
import com.sun.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 19:19
 **/
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospotalRepository;

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospitalByHoscode = hospotalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode;
    }

    @Override
    public void save(Map<String, Object> paramMap) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),
                Hospital.class);
        String hoscode = hospital.getHoscode();
        Hospital hospitalByHoscode = hospotalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode != null) {
            hospital.setStatus(hospitalByHoscode.getStatus());
            hospital.setCreateTime(hospitalByHoscode.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospotalRepository.save(hospital);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospotalRepository.save(hospital);
        }
    }


}
