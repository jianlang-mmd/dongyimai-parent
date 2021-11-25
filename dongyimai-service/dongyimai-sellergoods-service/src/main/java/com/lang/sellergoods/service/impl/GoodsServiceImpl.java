package com.lang.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.sellergoods.dao.*;
import com.lang.sellergoods.entity.GoodsEntity;
import com.lang.sellergoods.pojo.*;
import com.lang.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 增加Goods
     *
     * @param goodsEntity
     */
    @Override
    public void add(GoodsEntity goodsEntity) {
        goodsEntity.getGoods().setAuditStatus("0");  //审核状态 未审核
        //1.保存SPU 商品信息对象
        goodsMapper.insert(goodsEntity.getGoods());
        //2.获取商品信息对象主键ID ,向商品扩展信息对象中设置主键
        goodsEntity.getGoodsDesc().setGoodsId(goodsEntity.getGoods().getId());
        //3.保存商品扩展信息
        goodsDescMapper.insert(goodsEntity.getGoodsDesc());

        //4.保存SKU 商品详情信息  是否启用规格
        if ("1".equals(goodsEntity.getGoods().getIsEnableSpec())) {
            //5.保存SKU 商品详情信息
            if (!CollectionUtils.isEmpty(goodsEntity.getItemList())) {
                for (Item item : goodsEntity.getItemList()) {
                    String title = goodsEntity.getGoods().getGoodsName();
                    //设置SKU的名称  商品名+规格选项
                    Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);    //取得SKU的规格选项，并做JSON类型转换
                    for (String key : specMap.keySet()) {
                        title += specMap.get(key) + " ";
                    }
                    item.setTitle(title);                                       //SKU名称
                    item.setCategoryId(goodsEntity.getGoods().getCategory3Id());     //商品分类 三级
                    item.setCreateTime(new Date());                             //创建时间
                    item.setUpdateTime(new Date());                             //更新时间
                    item.setGoodsId(goodsEntity.getGoods().getId());                   //SPU ID
                    item.setSellerId(goodsEntity.getGoods().getSellerId());           //商家ID
                    //查询分类对象
                    ItemCat itemCat = itemCatMapper.selectById(goodsEntity.getGoods().getCategory3Id());
                    item.setCategory(itemCat.getName());                            //分类名称
                    //查询品牌对象
                    Brand tbBrand = brandMapper.selectById(goodsEntity.getGoods().getBrandId());
                    item.setBrand(tbBrand.getName());                               //品牌名称
                    List<Map> imageList = JSON.parseArray(goodsEntity.getGoodsDesc().getItemImages(), Map.class);
                    if (imageList.size() > 0) {
                        item.setImage((String) imageList.get(0).get("url"));           //商品图片
                    }
                    //保存SKU信息
                    itemMapper.insert(item);

                }
            }
        } else {
            //不启用规格  SKU信息为默认值
            Item item = new Item();
            item.setTitle(goodsEntity.getGoods().getGoodsName());     //商品名称
            item.setPrice(goodsEntity.getGoods().getPrice());      //默认使用SPU的价格
            item.setNum(9999);
            item.setStatus("1");            //是否启用
            item.setIsDefault("1");         //是否默认
            item.setSpec("{}");             //没有选择规格，则放置空JSON结构

            item.setGoodsId(goodsEntity.getGoods().getId());

            itemMapper.insert(item);
        }
    }

    /**
     * 根据ID查询Goods
     *
     * @param id
     * @return
     */
    @Override
    public GoodsEntity findById(Long id) {
        //1.根据ID查询SPU信息
        Goods goods = goodsMapper.selectById(id);
        //2.根据ID查询商品扩展信息
        GoodsDesc goodsDesc = goodsDescMapper.selectById(id);
        //3.根据ID查询SKU列表
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("goods_id", id);
        List<Item> itemList = itemMapper.selectList(queryWrapper);

        //4.设置复合实体对象
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods(goods);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setItemList(itemList);
        return goodsEntity;
    }

    /***
     * 商品审核
     * @param goodsId
     */
    @Override
    public void audit(Long goodsId) {
        //查询商品
        Goods goods = goodsMapper.selectById(goodsId);
        if(goods==null){
            throw new RuntimeException("商品不存在");
        }
        //判断商品是否已经删除
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("该商品已经删除！");
        }
        //实现上架和审核
        goods.setAuditStatus("1"); //审核通过
        goods.setIsMarketable("1"); //上架
        goodsMapper.updateById(goods);
    }

    /**
     * 商品下架
     * @param goodsId
     */
    @Override
    public void pull(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("此商品已删除！");
        }
        goods.setIsMarketable("0");//下架状态
        goodsMapper.updateById(goods);
    }

    /***
     * 商品上架
     * @param goodsId
     */
    @Override
    public void put(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        //检查是否删除的商品
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("此商品已删除！");
        }
        if(!goods.getAuditStatus().equals("1")){
            throw new RuntimeException("未通过审核的商品不能！");
        }
        //上架状态
        goods.setIsMarketable("1");
        goodsMapper.updateById(goods);
    }

    /***
     * 批量上架
     * @param ids:需要上架的商品ID集合
     * @return
     */
    @Override
    public int putMany(Long[] ids) {
        Goods goods=new Goods();
        goods.setIsMarketable("1");//上架
        //批量修改
        QueryWrapper<Goods> queryWrapper = new QueryWrapper();

        queryWrapper.in("id", Arrays.asList(ids));
        //下架
        queryWrapper.eq("is_marketable","0");
        //审核通过的
        queryWrapper.eq("audit_status","1");
        //非删除的
        queryWrapper.eq("is_delete","0");
        return goodsMapper.update(goods, queryWrapper);
    }

    /**
     * 删除，必须是下架的商品才能删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 逻辑删除
        Goods goods = goodsMapper.selectById(id);
        //检查是否下架的商品
        if (!goods.getIsMarketable().equals("0")) {
            throw new RuntimeException("必须先下架再删除！");
        }
        goods.setIsDelete("1");
        //设置成未审核状态
        goods.setAuditStatus("0");
        goodsMapper.updateById(goods);
    }

}

