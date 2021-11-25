package com.lang.seckill;

import com.lang.seckill.task.MultiThreadingCreateOrder;
import com.lang.util.IdWorker;
import com.lang.util.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients
@MapperScan(basePackages = "com.lang.seckill.dao")
@EnableScheduling //定时任务
@EnableAsync
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Bean
    public MultiThreadingCreateOrder multiThreadingCreateOrder() {
        return new MultiThreadingCreateOrder();
    }
}
