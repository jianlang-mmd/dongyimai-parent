package com.lang.sellergoods.service;

import com.lang.sellergoods.entity.GoodsEntity;

public interface GoodsService {
    /***
     * 新增Goods
     * @param goodsEntity
     */
    void add(GoodsEntity goodsEntity);

    /**
     * 根据ID查询Goods
     * @param id
     * @return
     */
    GoodsEntity findById(Long id);

    /***
     * 商品审核
     * @param goodsId
     */
    void audit(Long goodsId);

    /***
     * 商品下架
     * @param goodsId
     */
    void pull(Long goodsId);

    /***
     * 商品上架
     * @param goodsId
     */
    void put(Long goodsId);

    int putMany(Long[] ids);

    void delete(Long id);

}
