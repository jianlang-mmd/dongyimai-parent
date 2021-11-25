package com.lang.seckill.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lang.seckill.dao.SeckillGoodsMapper;
import com.lang.seckill.pojo.SeckillGoods;
import com.lang.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /****
     * 每30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis() {
        //获得时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        //循环时间段集合
        for (Date startTime : dateMenus) {

            //提取开始时间，转换为年月日格式的字符串
            String extName = DateUtil.date2Str(startTime);
            //创建查询条件对象
            QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
            //设置查询条件 1)商品必须审核通过  status=1
            queryWrapper.eq("status", "1");
            //2)库存大于0
            queryWrapper.gt("stock_count", 0);
            //3)活动开始时间（数据库） >= 时间段起始时间
            queryWrapper.ge("start_time", DateUtil.date2StrFull(startTime));
            //4)活动结束时间<起始时间+2小时
            queryWrapper.lt("end_time", DateUtil.date2StrFull(DateUtil.addDateHour(startTime, 2)));

            //5)读取redis中存在的当天的秒杀商品
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();

            //判断keys不为空，就设置排除条件
            if (keys != null && keys.size() > 0) {
                queryWrapper.notIn("id", keys);
            }

            //查询符合条件的数据
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(queryWrapper);

            //遍历查询到数据集合,存储数据到redis，一个时间段对应一个hash
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                redisTemplate.boundHashOps("SeckillGoods_" + extName)
                        .put(seckillGoods.getId(), seckillGoods);
            }
            //设置超时时间2小时
            redisTemplate.expireAt("SeckillGoods_" + extName, DateUtil.addDateHour(startTime, 2));
        }


    }
}
