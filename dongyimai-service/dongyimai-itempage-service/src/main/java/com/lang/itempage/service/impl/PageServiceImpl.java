//package com.lang.itempage.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.lang.entity.Result;
//import com.lang.itempage.service.PageService;
//import com.lang.sellergoods.entity.GoodsEntity;
//import com.lang.sellergoods.feign.GoodsFeign;
//import com.lang.sellergoods.feign.ItemCatFeign;
//import com.lang.sellergoods.pojo.Goods;
//import com.lang.sellergoods.pojo.GoodsDesc;
//import com.lang.sellergoods.pojo.Item;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class PageServiceImpl implements PageService {
//
//    @Autowired
//    ItemCatFeign itemCatFeign;
//
//    @Autowired
//    GoodsFeign goodsFeign;
//
//    @Value("${pagepath}")
//    String pagepath;
//
//    @Autowired
//    TemplateEngine templateEngine;
//
//    @Override
//    public void createPageHtml(Long spuId) {
//
//        Map<String, Object> dataModel = this.buildDataModel(spuId);
//
//        Context context = new Context();
//        context.setVariables(dataModel);// key-value
//
//        //将来生成html文件存储在该目录下
//        File dir = new File(pagepath);
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//
//        File file = new File(dir, spuId + ".html");//html文件
//
//        try {
//            PrintWriter printWriter = new PrintWriter(file, "utf-8");
//            templateEngine.process("item",context,printWriter);
//            printWriter.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void deletePageHtml(Long spuId) {
//        File file = new File(pagepath, spuId + ".html");
//        file.delete();
////        new File(pagepath+"/" + spuId + ".html");
//    }
//
//
//    /**
//     * 详情页模板上要使用的数据（商品详细信息，分类信息..）
//     * @param spuId
//     * @return
//     */
//    private Map<String,Object> buildDataModel(Long spuId){
//        Map<String,Object> dataModel = new HashMap<>();
//
//        Result<GoodsEntity> result = goodsFeign.findById(spuId);
//        GoodsEntity goodsEntity = result.getData();
//
//        Goods goods = goodsEntity.getGoods();
//        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
//        List<Item> itemList = goodsEntity.getItemList();
//
//
//        String category1 = itemCatFeign.findById(goods.getCategory1Id()).getData().getName();
//        String category2 = itemCatFeign.findById(goods.getCategory2Id()).getData().getName();
//        String category3 = itemCatFeign.findById(goods.getCategory3Id()).getData().getName();
//
//        // 规格列表 [{"attributeValue":["移动3G","移动4G"],"attributeName":"网络"},{"attributeValue":["16G","32G"],"attributeName":"机身内存"}]
//
//        List<Map> specificationItems = JSON.parseArray(goodsDesc.getSpecificationItems(), Map.class);
//
//        // 图片列表
//        List<Map> itemImages = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
//
//
//        dataModel.put("category1",category1);
//        dataModel.put("category2",category2);
//        dataModel.put("category3",category3);
//
//        dataModel.put("goods",goods);
//        dataModel.put("goodsDesc",goodsDesc);
//        dataModel.put("itemList",itemList);
//
//
//        dataModel.put("specificationList",specificationItems);
//        dataModel.put("imageList",itemImages);// [{},{color,url}]
//
//
//
//        return dataModel;
//    }
//}
