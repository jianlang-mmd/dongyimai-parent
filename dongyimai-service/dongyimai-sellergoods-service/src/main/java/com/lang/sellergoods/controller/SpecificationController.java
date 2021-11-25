package com.lang.sellergoods.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.entity.SpecEntity;
import com.lang.sellergoods.service.SpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Api(tags = "SpecificationController")
@RestController
@RequestMapping("/specification")
@CrossOrigin
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /***
     * 新增Specification数据
     * @param specEntity
     * @return
     */
    @ApiOperation(value = "Specification添加", notes = "添加Specification方法详情", tags = {"SpecificationController"})
    @PostMapping
    public Result add(@RequestBody @ApiParam(name = "SpecEntity复合实体", value = "传入JSON数据", required = true) SpecEntity specEntity) {
        //调用SpecificationService实现添加Specification
        specificationService.add(specEntity);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    @ApiOperation(value = "Specification根据ID查询",notes = "根据ID查询Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")

    @GetMapping("/{id}")
    public Result<SpecEntity> findById(@PathVariable("id") Long id){
        //调用SpecificationService实现根据主键查询Specification
        SpecEntity specification = specificationService.findById(id);
        return new Result<SpecEntity>(true, StatusCode.OK,"查询成功",specification);
    }

    /***
     * 修改Specification数据
     * @param specEntity
     * @param id
     * @return
     */
    @ApiOperation(value = "Specification根据ID修改",notes = "根据ID修改Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")

    @PutMapping(value="/{id}")
    public Result update(@RequestBody @ApiParam(name = "Specification对象",value = "传入JSON数据",required = false) SpecEntity specEntity,@PathVariable("id") Long id){
        //设置主键值
        specEntity.getSpecification().setId(id);
        specificationService.update(specEntity);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Specification根据ID删除",notes = "根据ID删除Specification方法详情",tags = {"SpecificationController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpecificationService实现根据主键删除
        specificationService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @ApiOperation(value = "查询规格下拉列表",notes = "查询规格下拉列表",tags = {"SpecificationController"})
    @GetMapping("/selectOptions")
    public List<Map> selectOptions() {
        return specificationService.selectOptions();
    }
}
