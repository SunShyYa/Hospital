package com.sun.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.yygh.common.exception.YyghException;
import com.sun.yygh.common.result.ResultCodeEnum;
import com.sun.yygh.hosp.mapper.HospitalSetMapper;
import com.sun.yygh.hosp.service.HospitalSetService;
import com.sun.yygh.model.hosp.HospitalSet;
import com.sun.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 13:39
 **/
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if (hospitalSet == null) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
