package com.lang.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class SendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public void simpleQueue() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "18586941512");
        map.put("code", "9999");
        rabbitTemplate.convertAndSend("dongyimai.sms.queue", map);
    }
}
