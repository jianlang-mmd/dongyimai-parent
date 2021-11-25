package com.lang.sellergoods.service;

import com.lang.sellergoods.pojo.Item;

import java.util.List;

public interface ItemService {
    /**
     * 根据状态查询SKU列表（将审核通过的sku商品导入到es中）
     */
    List<Item> findByStatus(String status);

    /***
     * 库存递减
     * @param username
     */
    void decrCount(String username);
}
