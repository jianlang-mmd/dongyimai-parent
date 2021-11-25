package com.lang.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.order.pojo.OrderItem;
import com.lang.order.util.Cart;
import com.lang.sellergoods.dao.ItemMapper;
import com.lang.sellergoods.pojo.Item;
import com.lang.sellergoods.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Item> findByStatus(String status) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return this.list(queryWrapper);
    }

    /***
     * 库存递减
     * @param username
     */
    @Override
    public void decrCount(String username) {

        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);

        for (Cart cart : cartList) {
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                Long itemId = orderItem.getItemId();//sku商品的id
                Integer num = orderItem.getNum();//商品数量，扣减num个
                Item item = this.getById(itemId);
                item.setNum(item.getNum() - num);
                this.updateById(item);
            }
        }
    }
}
