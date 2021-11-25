package com.lang.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicQueueConfig {
    /**
     * 声明队列名.
     */
    private final String keqing_d1 = "KeQing_1";
    private final String keqing_d2 = "KeQing_2";

    /**
     * 声明交换机的名字.
     */
    private final String hutao = "HuTao_EX";

    /**
     * 声明队列.
     *
     * @return
     */
    @Bean
    public Queue KeQing_1() {
        return new Queue(keqing_d1);
    }

    @Bean
    public Queue KeQing_2() {
        return new Queue(keqing_d2);
    }

    /**
     * 声明路由交换机.
     *
     * @return
     */
    @Bean
    public TopicExchange hutao() {
        return new TopicExchange(hutao);
    }

    /**
     * 队列绑定交换机,指定routingKey,也可在可视化工具中进行绑定.
     *
     * @return
     */
    @Bean
    Binding bindingTopicExchange1(Queue KeQing_1, TopicExchange HuTao_EX) {
        return BindingBuilder.bind(KeQing_1).to(HuTao_EX).with("topic.keyA");
    }

    /**
     * 队列绑定交换机,指定routingKey,也可在可视化工具中进行绑定.
     * 绑定的routing key 也可以使用通配符：
     * *：匹配不多不少一个词
     * #：匹配一个或多个词
     *
     * @return
     */
    @Bean
    Binding bindingTopicExchange2(Queue KeQing_2, TopicExchange HuTao_EX) {
        return BindingBuilder.bind(KeQing_2).to(HuTao_EX).with("topic.#");
    }

}
