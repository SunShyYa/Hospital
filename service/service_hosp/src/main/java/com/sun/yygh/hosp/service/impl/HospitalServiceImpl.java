package com.sun.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.yygh.cmn.client.DictFeignClient;
import com.sun.yygh.hosp.repository.HospitalRepository;
import com.sun.yygh.hosp.service.HospitalService;
import com.sun.yygh.model.hosp.Hospital;
import com.sun.yygh.model.hosp.HospitalSet;
import com.sun.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private DictFeignClient feignClient;

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospotalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospotalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        result.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospotalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public String getHosName(String hoscode) {
        Hospital hospitalByHoscode = hospotalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode.getHosname();
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Hospital hospital = hospotalRepository.findById(id).get();
        this.setHospitalHosType(hospital);
        Map<String, Object> map = new HashMap<>();
        map.put("hospital", hospital);
        map.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return map;
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> all = hospotalRepository.findAll(example, pageable);
        all.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });
        return all;
    }

    private Hospital setHospitalHosType(Hospital item) {
        String hostype = feignClient.getName("hostype", item.getHostype());
        String province = feignClient.getName(item.getProvinceCode());
        String city = feignClient.getName(item.getCityCode());
        String dictrict = feignClient.getName(item.getDistrictCode());
        item.getParam().put("fullAddress",province + city + dictrict);
        item.getParam().put("hostypeString", hostype);
        return item;
    }

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
