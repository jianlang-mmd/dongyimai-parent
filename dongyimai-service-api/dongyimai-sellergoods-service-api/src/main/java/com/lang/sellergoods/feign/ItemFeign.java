package com.lang.sellergoods.feign;

import com.lang.sellergoods.pojo.Item;
import com.lang.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "dym-sellergoods")
@RequestMapping("/item")
//@RequestMapping(value = "/item")
public interface ItemFeign {

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/item/status/{status}")
    Result<List<Item>> findByStatus(@PathVariable("status") String status);

    /***
     * 根据ID查询Item数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Item> findById(@PathVariable("id") Long id);

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestParam(value = "username") String username);
}