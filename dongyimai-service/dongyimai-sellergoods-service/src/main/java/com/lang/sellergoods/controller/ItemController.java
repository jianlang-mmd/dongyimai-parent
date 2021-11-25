package com.lang.sellergoods.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.pojo.Item;
import com.lang.sellergoods.service.ItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ItemCatController")
@RestController
@RequestMapping("/itemCat")
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemService itemService;

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Item>> findByStatus(@PathVariable String status) {
        List<Item> list = itemService.findByStatus(status);
        return new Result<>(true, StatusCode.OK, "查询成功！", list);
    }

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    public Result decrCount(@RequestParam(value = "username") String username) {
        //库存递减
        itemService.decrCount(username);
        return new Result(true, StatusCode.OK, "库存递减成功！");
    }
}
