package com.simple.web.controller.message;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfimAckNew implements RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("消息主体 message : "+message);
        System.out.println("消息主体 message : "+i);
        System.out.println("描述："+s);
        System.out.println("消息使用的交换器 exchange : "+s1);
        System.out.println("消息使用的路由键 routing : "+s2);
    }
}
