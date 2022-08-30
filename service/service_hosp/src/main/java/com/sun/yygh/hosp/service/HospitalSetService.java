package com.sun.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.hosp.HospitalSet;
import com.sun.yygh.vo.order.SignInfoVo;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 13:38
 **/
public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);
    SignInfoVo getSignInfoVo(String hoscode);
}
