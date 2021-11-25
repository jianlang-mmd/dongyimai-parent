package com.lang.sellergoods.controller;


import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.pojo.ItemCat;
import com.lang.sellergoods.service.ItemCatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "ItemCatController")
@RestController
@RequestMapping("/itemCat")
@CrossOrigin
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @ApiOperation(value = "根据父级ID查询ItemCat",notes = "根据父级ID查询ItemCat",tags = {"ItemCatController"})
    @GetMapping("/findByParentId")
    public Result<List<ItemCat>> findByParentId(Long parentId) {
        List<ItemCat> list = itemCatService.findByParentId(parentId);
        return new Result<>(true, StatusCode.OK,"查询成功",list) ;
    }


}
