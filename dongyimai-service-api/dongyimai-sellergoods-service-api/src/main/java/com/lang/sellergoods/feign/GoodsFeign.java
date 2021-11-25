package com.lang.sellergoods.feign;

import com.lang.entity.Result;
import com.lang.sellergoods.entity.GoodsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "dym-sellergoods")
@RequestMapping("/goods")
public interface GoodsFeign {
    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<GoodsEntity> findById(@PathVariable Long id);

}
