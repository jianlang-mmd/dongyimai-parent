package com.lang.sellergoods.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.pojo.TypeTemplate;
import com.lang.sellergoods.service.TypeTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "TypeTemplateController")
@RestController
@RequestMapping("/typeTemplate")
@CrossOrigin
public class TypeTemplateController {

    @Autowired
    private TypeTemplateService typeTemplateService;
    /***
     * 根据ID查询TypeTemplate数据（品牌）
     * @param id
     * @return
     */
    @ApiOperation(value = "TypeTemplate根据ID查询",notes = "根据ID查询TypeTemplate方法详情",tags = {"TypeTemplateController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result<TypeTemplate> findById(@PathVariable Long id){
        //调用TypeTemplateService实现根据主键查询TypeTemplate
        TypeTemplate typeTemplate = typeTemplateService.findById(id);
        return new Result<>(true, StatusCode.OK,"查询成功",typeTemplate);
    }

    @ApiOperation(value = "查询规格及规格选项信息",notes = "查询规格及规格选项信息",tags = {"TypeTemplateController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Long")
    @GetMapping("/findSpecList/{id}")
    public Result<List<Map>> findSpecList(@PathVariable Long id){
        List<Map> list =  typeTemplateService.findSpecList(id);
        return new Result<>(true, StatusCode.OK,"查询成功",list) ;
    }
}
