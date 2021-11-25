package com.lang.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.sellergoods.dao.SpecificationOptionMapper;
import com.lang.sellergoods.entity.SpecEntity;
import com.lang.sellergoods.pojo.Specification;
import com.lang.sellergoods.pojo.SpecificationOption;
import com.lang.sellergoods.service.SpecificationService;
import com.lang.sellergoods.dao.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Specification业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
    @Autowired
    private SpecificationMapper specificationMapper;
     /**
     * 增加Specification
     * @param specEntity
     */
    @Override
    public void add(SpecEntity specEntity) {
        //1.保存规格名称
        this.save(specEntity.getSpecification());
        //2.得到规格名称ID
        if (null != specEntity.getSpecificationOptionList()&&specEntity.getSpecificationOptionList().size() > 0) 	 {
            for (SpecificationOption specificationOption : specEntity.getSpecificationOptionList()) {
                //3.向规格选项中设置规格ID
                specificationOption.setSpecId(specEntity.getSpecification().getId());
                //4.保存规格选项
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    @Override
    public SpecEntity findById(Long id) {
        Specification specification = specificationMapper.selectById(id);

        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();
        queryWrapper.eq("spec_id",id);
        List<SpecificationOption> specificationOptionList =  specificationOptionMapper.selectList(queryWrapper);

        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecificationOptionList(specificationOptionList);

        return specEntity;
    }

    /**
     * 修改Specification
     * @param specEntity
     */
    @Override
    public void update(SpecEntity specEntity){
        //1.修改规格名称对象
        this.updateById(specEntity.getSpecification());
        //2.根据ID删除规格选项集合
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<SpecificationOption>();
        queryWrapper.eq("spec_id", specEntity.getSpecification().getId());
        specificationOptionMapper.delete(queryWrapper);
        //3.重新插入规格选项
        if (!CollectionUtils.isEmpty(specEntity.getSpecificationOptionList())) {
            for (SpecificationOption specificationOption : specEntity.getSpecificationOptionList()) {
                //先设置规格名称的ID
                specificationOption.setSpecId(specEntity.getSpecification().getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        //1.删除规格名称对象
        this.removeById(id);
        //2.关联删除规格选项集合
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();
        queryWrapper.eq("spec_id", id);
        //执行删除
        specificationOptionMapper.delete(queryWrapper);
    }

    /**
     * 查询规格下拉列表
     *
     * @return
     */
    @Override
    public List<Map> selectOptions() {
        return specificationMapper.selectOptions();
    }
}
