package com.lang.sellergoods.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.entity.GoodsEntity;
import com.lang.sellergoods.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "GoodsController")
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    /***
     * 新增Goods数据
     * @param goodsEntity
     * @return
     */
    @ApiOperation(value = "Goods添加",notes = "添加Goods方法详情",tags = {"GoodsController"})
    @PostMapping
    public Result add(@RequestBody @ApiParam(name = "Goods复合实体对象",value = "传入JSON数据",required = true) GoodsEntity goodsEntity){
        //调用GoodsService实现添加Goods
        goodsService.add(goodsEntity);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Goods数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Goods根据ID查询",notes = "根据ID查询Goods方法详情",tags = {"GoodsController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result<GoodsEntity> findById(@PathVariable Long id){
        //调用GoodsService实现根据主键查询Goods
        GoodsEntity goodsEntity = goodsService.findById(id);
        return new Result<>(true,StatusCode.OK,"查询成功",goodsEntity);
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable Long id){
        goodsService.audit(id);
        return new Result(true,StatusCode.OK,"审核成功");
    }

    /**
     * 下架
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable Long id){
        goodsService.pull(id);
        return new Result(true,StatusCode.OK,"下架成功");
    }

    /**
     * 商品上架
     * @param id
     * @return
     */
    @PutMapping("/put/{id}")
    public Result put(@PathVariable Long id){
        goodsService.put(id);
        return new Result(true,StatusCode.OK,"上架成功");
    }

    /**
     *  批量上架
     * @param ids
     * @return
     */
    @PutMapping("/put/many")
    public Result putMany(@RequestBody Long[] ids){
        int count = goodsService.putMany(ids);
        return new Result(true,StatusCode.OK,"上架"+count+"个商品");
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Goods根据ID删除",notes = "根据ID删除Goods方法详情",tags = {"GoodsController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID集合", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        //调用GoodsService实现根据主键删除
        goodsService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

}
