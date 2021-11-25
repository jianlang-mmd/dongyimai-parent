package com.lang.seckill.task;

import com.lang.seckill.pojo.SeckillOrder;
import com.lang.seckill.vo.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private SeckillOrder seckillOrder; 工厂创建实例，或者手动new一个（@Component）

    /***
     * 多线程下单操作
     */
    @Async
    public void createOrder(String username) {
        System.out.println("准备执行....");
        //从队列中获取排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate
                .boundListOps("SeckillOrderQueue").rightPop();

        try {
            if (seckillStatus != null) {
                //抢单成功 更新抢单状态 排队=>支付
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillStatus.setStatus(2);
                seckillStatus.setOrderId(seckillOrder.getId());
                seckillStatus.setMoney(seckillOrder.getMoney().floatValue());
                redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
