package com.simple.web.controller.RabbitConsumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class TopicReceiver {

//    @RabbitListener(queues = "topic.woman")//监听的队列名称 TestDirectQueue
//    @RabbitHandler
//    public void process(Message testMessage) {
//        System.out.println("woman开始消费消息");
//        System.out.println("TopicReceiver消费者收到消息  : " + testMessage);
//    }
//
//    @RabbitListener(queues = "topic.man")//监听的队列名称 TestDirectQueue
//    @RabbitHandler
//    public void process1(Message testMessage) {
//        System.out.println("man开始消费消息");
//        System.out.println("TopicReceiver消费者收到消息  : " + testMessage);
//    }
}
