package com.lang.sellergoods.service;

import com.lang.sellergoods.entity.SpecEntity;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
    /***
     * 新增Specification
     * @param specEntity
     */
    void add(SpecEntity specEntity);

    SpecEntity findById(Long id);

    /***
     * 修改Specification数据
     * @param specEntity
     */
    void update(SpecEntity specEntity);

    void delete(Long id);

    /**
     * 查询规格下拉列表
     * @return
     */
    public List<Map> selectOptions();
}
