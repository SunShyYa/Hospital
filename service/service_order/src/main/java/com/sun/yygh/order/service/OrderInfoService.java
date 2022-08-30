package com.sun.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.order.OrderInfo;
import com.sun.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 19:58
 **/
public interface OrderInfoService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
    OrderInfo getOrderInfo(String id);
    Map<String,Object> show(Long orderId);
}
