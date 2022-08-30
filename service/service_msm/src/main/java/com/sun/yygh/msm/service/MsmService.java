package com.sun.yygh.msm.service;

import com.sun.yygh.vo.msm.MsmVo;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-28 14:17
 **/
public interface MsmService {
    boolean send(String phone, String code);
    boolean send(MsmVo msmVo);
}

