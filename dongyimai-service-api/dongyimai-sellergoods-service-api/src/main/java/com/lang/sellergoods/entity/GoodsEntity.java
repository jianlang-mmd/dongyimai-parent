package com.lang.sellergoods.entity;

import com.lang.sellergoods.pojo.Goods;
import com.lang.sellergoods.pojo.GoodsDesc;
import com.lang.sellergoods.pojo.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsEntity implements Serializable {
    private Goods goods;      //商品信息 SPU
    private GoodsDesc goodsDesc;   //商品扩展信息
    private List<Item> itemList;   //商品详情 SKU

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
