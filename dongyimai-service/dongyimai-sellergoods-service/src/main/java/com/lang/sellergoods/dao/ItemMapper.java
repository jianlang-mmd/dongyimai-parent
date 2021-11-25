package com.lang.sellergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.lang.sellergoods.pojo.Item;
import org.apache.ibatis.annotations.Update;

public interface ItemMapper extends BaseMapper<Item> {
    /**
     * 递减库存
     *
     * @param orderItem
     * @return UPDATE `tb_item` SET num=num-5 WHERE id = ? AND num>=5
     */
    @Update("UPDATE tb_item SET num=num-#{num} WHERE id=#{itemId} AND num>=#{num}")
    public int decrCount(OrderItem orderItem);
}
