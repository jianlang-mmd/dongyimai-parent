package com.lang.sellergoods.feign;

import com.lang.entity.Result;
import com.lang.sellergoods.pojo.ItemCat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "dym-sellergoods")
@RequestMapping
public interface ItemCatFeign {
    /**
     * 获取分类的对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ItemCat> findById(@PathVariable("id") Integer id);
}
