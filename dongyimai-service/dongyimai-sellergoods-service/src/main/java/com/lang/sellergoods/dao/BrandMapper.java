package com.lang.sellergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lang.sellergoods.pojo.Brand;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 用来查询品牌列表，给前端页面添加模板弹出框上的一个关联品牌控件中显示
     * @return
     */
    @Select("SELECT id,NAME AS 'text' FROM `tb_brand`")
    public List<Map> selectOptions();
}
