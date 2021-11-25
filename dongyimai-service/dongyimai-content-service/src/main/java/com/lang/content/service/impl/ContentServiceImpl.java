package com.lang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lang.entity.PageResult;
import com.lang.content.dao.ContentMapper;
import com.lang.content.pojo.Content;
import com.lang.content.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:Content业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper,Content> implements ContentService {


    /**
     * Content条件+分页查询
     * @param content 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Content> findPage(Content content, int page, int size){
         Page<Content> mypage = new Page<>(page, size);
        QueryWrapper<Content> queryWrapper = this.createQueryWrapper(content);
        IPage<Content> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Content>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Content分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Content> findPage(int page, int size){
        Page<Content> mypage = new Page<>(page, size);
        IPage<Content> iPage = this.page(mypage, new QueryWrapper<Content>());

        return new PageResult<Content>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Content条件查询
     * @param content
     * @return
     */
    @Override
    public List<Content> findList(Content content){
        //构建查询条件
        QueryWrapper<Content> queryWrapper = this.createQueryWrapper(content);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Content构建查询对象
     * @param content
     * @return
     */
    public QueryWrapper<Content> createQueryWrapper(Content content){
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        if(content!=null){
            // 
            if(!StringUtils.isEmpty(content.getId())){
                 queryWrapper.eq("id",content.getId());
            }
            // 内容类目ID
            if(!StringUtils.isEmpty(content.getCategoryId())){
                 queryWrapper.eq("category_id",content.getCategoryId());
            }
            // 内容标题
            if(!StringUtils.isEmpty(content.getTitle())){
                queryWrapper.like("title",content.getTitle());
            }
            // 链接
            if(!StringUtils.isEmpty(content.getUrl())){
                 queryWrapper.eq("url",content.getUrl());
            }
            // 图片绝对路径
            if(!StringUtils.isEmpty(content.getPic())){
                 queryWrapper.eq("pic",content.getPic());
            }
            // 状态
            if(!StringUtils.isEmpty(content.getStatus())){
                 queryWrapper.eq("status",content.getStatus());
            }
            // 排序
            if(!StringUtils.isEmpty(content.getSortOrder())){
                 queryWrapper.eq("sort_order",content.getSortOrder());
            }
        }
        return queryWrapper;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        this.removeById(id);
    }

    /**
     * 修改Content
     * @param content
     */
    @Override
    public void update(Content content){
        this.updateById(content);
    }

    /**
     * 增加Content
     * @param content
     */
    @Override
    public void add(Content content){
        this.save(content);
    }

    /**
     * 根据ID查询Content
     * @param id
     * @return
     */
    @Override
    public Content findById(Long id){
        return  this.getById(id);
    }

    /**
     * 查询Content全部数据
     * @return
     */
    @Override
    public List<Content> findAll() {
        return this.list(new QueryWrapper<Content>());
    }

    @Override
    public List<Content> findByCategory(Long id) {
        //创建查询条件数据封装对象
        Content content = new Content();
        //设置广告分类id查询条件
        content.setCategoryId(id);
        //设置广告状态为 1 可用
        content.setStatus("1");
        //构建查询条件
        QueryWrapper<Content> queryWrapper = this.createQueryWrapper(content);
        //设置按照排序字段升序进行排序
        queryWrapper.orderByAsc("sort_order");
        //发出查询
        return this.list(queryWrapper);
    }
}
