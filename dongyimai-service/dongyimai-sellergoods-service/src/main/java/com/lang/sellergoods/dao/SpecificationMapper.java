package com.lang.sellergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lang.sellergoods.pojo.Specification;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends BaseMapper<Specification> {
    @Select("select id,spec_name as text from tb_specification")
    public List<Map> selectOptions();
}
