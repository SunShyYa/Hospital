package com.sun.yygh.hosp.client;

import com.sun.yygh.vo.hosp.ScheduleOrderVo;
import com.sun.yygh.vo.order.SignInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 21:01
 **/
@FeignClient("service-hosp")
@Service
public interface HospitalFeignClient {
    /**
     * 根据排班id获取预约下单数据
     */
    @GetMapping("/api/hosp/hospital/inner/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId);
    /**
     * 获取医院签名信息
     */
    @GetMapping("/api/hosp/hospital/inner/getSignInfoVo/{hoscode}")
    SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode);

}
