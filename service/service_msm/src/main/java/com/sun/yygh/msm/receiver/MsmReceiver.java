package com.sun.yygh.msm.receiver;

import com.sun.yygh.msm.service.MsmService;
import com.sun.yygh.rabbit.constant.MqConst;
import com.sun.yygh.vo.msm.MsmVo;
import org.apache.logging.log4j.message.Message;
import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 22:28
 **/
@Component
public class MsmReceiver {
    @Autowired
    private MsmService msmService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_MSM_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_MSM),
            key = {MqConst.ROUTING_MSM_ITEM}
    ))
    public void send(MsmVo msmVo, Message message, Channel channel) {
        msmService.send(msmVo);
    }
}
