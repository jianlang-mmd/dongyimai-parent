package com.lang.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.sellergoods.dao.ItemCatMapper;
import com.lang.sellergoods.pojo.Brand;
import com.lang.sellergoods.pojo.ItemCat;
import com.lang.sellergoods.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ItemCatServiceImpl extends ServiceImpl<ItemCatMapper, ItemCat> implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    /**
     * 根据父级ID查询分类列表
     *
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        QueryWrapper<ItemCat> queryWrapper = this.createQueryWrapper(itemCat);
        return this.list(queryWrapper);
    }

    private QueryWrapper<ItemCat> createQueryWrapper(ItemCat itemCat) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        if (itemCat != null) {

            if (itemCat.getId() != null) {
                queryWrapper.eq("id", itemCat.getId());
            }

            if (!StringUtils.isEmpty(itemCat.getName())) {
                queryWrapper.like("name", itemCat.getName());
            }
        }
        return queryWrapper;
    }
}
