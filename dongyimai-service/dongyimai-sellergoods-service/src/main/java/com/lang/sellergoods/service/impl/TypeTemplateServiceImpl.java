package com.lang.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.sellergoods.dao.SpecificationOptionMapper;
import com.lang.sellergoods.dao.TypeTemplateMapper;
import com.lang.sellergoods.pojo.SpecificationOption;
import com.lang.sellergoods.pojo.TypeTemplate;
import com.lang.sellergoods.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl extends ServiceImpl<TypeTemplateMapper, TypeTemplate> implements TypeTemplateService {

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 根据ID查询TypeTemplate
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findById(Long id){
        return  this.getById(id);
    }

    /**
     * 根据模板ID查询规格列表
     *
     * @param typeId
     * @return
     */
    public List<Map> findSpecList(Long typeId) {
        //1.根据模板ID查询模板对象
        TypeTemplate typeTemplate = this.findById(typeId);
        //2.将规格名称JSON结构的字符串转换成JSON对象   [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        List<Map> specList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
        if (!CollectionUtils.isEmpty(specList)) {
            for (Map map : specList) {
                Long specId = Long.parseLong(String.valueOf(map.get("id")));    //Map集合中取得数值类型的值默认是整型
                //3.根据规格ID查询规格选项集合
                QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();

                queryWrapper.eq("spec_id", specId);
                List<SpecificationOption> specificationOptionList = specificationOptionMapper.selectList(queryWrapper);
                //4.重新将规格选项集合设置回JSON对象中  {"id":27,"text":"网络","options":[{},{},{}]}
                map.put("options", specificationOptionList);
            }
        }
        return specList;
    }

}
