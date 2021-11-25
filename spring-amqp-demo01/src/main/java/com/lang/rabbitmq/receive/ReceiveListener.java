package com.lang.rabbitmq.receive;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveListener {
    //监听指定队列，按顺序获取队列中的消息数据

    @RabbitListener(queues = "spring.test.queue")
    public void receive(String message) {
        System.out.println(message);
    }

    //工人1，工作的慢
    @RabbitListener(queues = "work_queue")
    public void recieve1(String message) {
        System.out.println("listener1:" + message);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //工人2，工作的快
    @RabbitListener(queues = "work_queue")
    public void recieve2(String message) {
        System.out.println("listener2:" + message);
    }

    //fanout
    @RabbitListener(queues = "fanout_queue_1")
    public void recieve3(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "fanout_queue_2")
    public void recieve4(String message) {
        System.out.println("listener2:" + message);
    }

    //direct
    @RabbitListener(queues = "direct_queue_1")
    public void recieve5(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "direct_queue_2")
    public void recieve6(String message) {
        System.out.println("listener2:" + message);
    }

    //topic
    @RabbitListener(queues = "KeQing_1")
    public void recieve7(String message) {
        System.out.println("listener1:" + message);
    }

    @RabbitListener(queues = "KeQing_2")
    public void recieve8(String message) {
        System.out.println("listener1:" + message);
    }

}
