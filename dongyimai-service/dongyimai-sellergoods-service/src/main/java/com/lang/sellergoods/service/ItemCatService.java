package com.lang.sellergoods.service;

import com.lang.sellergoods.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {
    /**
     * 根据父级ID查询分类列表
     * @param parentId
     * @return
     */
    public List<ItemCat> findByParentId(Long parentId);


}
