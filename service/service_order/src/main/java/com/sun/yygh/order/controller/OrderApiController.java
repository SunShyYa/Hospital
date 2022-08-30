package com.sun.yygh.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.common.utils.AuthContextHolder;
import com.sun.yygh.enums.OrderStatusEnum;
import com.sun.yygh.model.order.OrderInfo;
import com.sun.yygh.order.service.OrderInfoService;
import com.sun.yygh.vo.order.OrderQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 20:06
 **/
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderInfoService orderService;


    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(@PathVariable String scheduleId,
                              @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> orderInfoIPage = orderService.selectPage(pageParam, orderQueryVo);
        return Result.ok(orderInfoIPage);

    }

    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
        return Result.ok(orderInfo);
    }

}
