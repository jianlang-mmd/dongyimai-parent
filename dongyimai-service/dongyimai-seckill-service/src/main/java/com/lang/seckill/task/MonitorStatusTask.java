package com.lang.seckill.task;

import com.lang.seckill.dao.SeckillGoodsMapper;
import com.lang.seckill.pojo.SeckillOrder;
import com.lang.seckill.vo.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MonitorStatusTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Async
    public void monitorPayStatus(String username, SeckillOrder seckillOrder) {
        int n = 0;
        while (true) {
            //
            SeckillStatus seckillStatus = (SeckillStatus)redisTemplate.boundHashOps("UserQueueStatus").get(username);
        }
    }
}
