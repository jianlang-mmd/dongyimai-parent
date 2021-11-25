package com.lang.rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public void send() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("spring.test.queue", "简单队列消息" + i);
        }
    }

    @RequestMapping("/work")
    public void work() {
        for (int i = 0; i < 50; i++) {
            String message = "工作队列消息" + i;
            rabbitTemplate.convertAndSend("work_queue", message);
        }
    }

    @RequestMapping("/fanout")
    public void fanout() {
        for (int i = 0; i < 5; i++) {
            String message = "订阅模式消息" + i;
            // 参数1：交换机名称  参数2：在fanout类型的交换机中不需要指定key   参数2：消息
            rabbitTemplate.convertAndSend("fanoutExchange", "fanout消息", message);
        }
    }

    @RequestMapping("/direct1")
    public void direct1() {
        for (int i = 0; i < 5; i++) {
            String message = "路由模式--routingKey=update消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("directExchange", "update", message);
        }
    }

    @RequestMapping("/direct2")
    public void direct2() {
        for (int i = 0; i < 5; i++) {
            String message = "路由模式--routingKey=add消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("directExchange", "add", message);
        }
    }

    @RequestMapping("/laopo1")
    public void KeQing_d1() {
        for (int i = 0; i < 5; i++) {
            String message = "通配符模式--routingKey=topic.keyA消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("HuTao", "topic.keyA", message);
        }
    }

    @RequestMapping("/laopo2")
    public void KeQing_d2() {
        for (int i = 0; i < 5; i++) {
            String message = "通配符模式--routingKey=topic.#消息" + i;
            System.out.println("我是生产信息的：" + message);
            rabbitTemplate.convertAndSend("HuTao", "topic.#", message);
        }
    }
}
