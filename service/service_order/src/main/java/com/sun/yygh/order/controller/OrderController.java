package com.sun.yygh.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.enums.OrderStatusEnum;
import com.sun.yygh.model.order.OrderInfo;
import com.sun.yygh.order.service.OrderInfoService;
import com.sun.yygh.vo.order.OrderQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-30 13:18
 **/
@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderController {
    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping("{page}/{limit}")
    public Result index (@PathVariable Long page,
                         @PathVariable Long limit,
                         OrderQueryVo orderQueryVo) {
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> orderInfoIPage = orderInfoService.selectPage(pageParam, orderQueryVo);
        return Result.ok(orderInfoIPage);
    }
    @GetMapping("getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @GetMapping("show/{id}")
    public Result get(@PathVariable Long id) {
        return Result.ok(orderInfoService.show(id));
    }
}
