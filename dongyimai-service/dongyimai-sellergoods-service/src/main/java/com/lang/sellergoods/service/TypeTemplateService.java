package com.lang.sellergoods.service;

import com.lang.sellergoods.pojo.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    /**
     * 根据ID查询TypeTemplate（brandIds）
     * @param id
     * @return
     */
    TypeTemplate findById(Long id);

    /**
     * 根据模板ID查询规格列表
     * @param typeId（模板id）
     * @return
     */
    List<Map> findSpecList(Long typeId);
}
