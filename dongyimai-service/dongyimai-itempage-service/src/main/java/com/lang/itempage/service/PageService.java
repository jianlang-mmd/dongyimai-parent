package com.lang.itempage.service;

public interface PageService {

    /**
     * 创建详情页  10026069818397.html
     * @param spuId  spu商品id
     */
    void createPageHtml(Long spuId);

    void deletePageHtml(Long spuId);
}
