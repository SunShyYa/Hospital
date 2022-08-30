package com.sun.yygh.hosp.receiver;

import com.rabbitmq.client.Channel;
import com.sun.yygh.hosp.service.ScheduleService;
import com.sun.yygh.model.hosp.Schedule;
import com.sun.yygh.rabbit.constant.MqConst;
import com.sun.yygh.rabbit.service.RabbitService;
import com.sun.yygh.vo.msm.MsmVo;
import com.sun.yygh.vo.order.OrderMqVo;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 22:38
 **/
@Component
public class HospitalReceiver {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER,durable = "true"),
    exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
    key = {MqConst.QUEUE_ORDER}))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) {
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        MsmVo msmVo = orderMqVo.getMsmVo();
        if (msmVo != null) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM,
                    msmVo);
        }
    }
}
