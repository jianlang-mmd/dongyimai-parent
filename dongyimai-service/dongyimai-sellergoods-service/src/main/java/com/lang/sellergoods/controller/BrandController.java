package com.lang.sellergoods.controller;

import com.lang.entity.PageResult;
import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.sellergoods.pojo.Brand;
import com.lang.sellergoods.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "BrandController")
@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询全部数据
     *
     * @Return
     */
    @ApiOperation(value = "查询所有", notes = "无条件无分页的查询所有品牌", tags = "BrandController")
    @GetMapping//不写value值默认调用根路径
    public Result<List<Brand>> findAll(HttpServletRequest request) {
        // 获取请求头中的Authorization（claims）
        String claims = request.getHeader("Authorization");
        System.out.println(claims);
        List<Brand> brandList = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", brandList);
    }

    /***
     * 根据ID查询Brand数据
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "根据主键id查询一个品牌对象", tags = "BrandController")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "主键", paramType = "path", dataType = "Long")
//                    @ApiImplicitParam(name = "age",value = "年龄",paramType = "request")
            }
    )
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable("id") Long id) {
//    public Result<Brand> findById(@PathVariable("id") Long id,@RequestParam("age") int age){
        try {
            Brand brand = brandService.findById(id);
            return new Result<>(true, StatusCode.OK, "根据id查询成功", brand);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(false, StatusCode.ERROR, "根据id查询失败");
        }
    }

    /***
     * 新增Brand数据
     * @pa am brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody(required = true) Brand brand) {
        try {
            brandService.add(brand);
            return new Result(true, StatusCode.OK, "添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加失败！" + e.getMessage());
        }
    }

    /***
     * 修改Brand数据
     * @param brand
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@RequestBody Brand brand, @PathVariable("id") Long id) {
        try {
            brand.setId(id);
            brandService.update(brand);
            return new Result(true, StatusCode.OK, "修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改失败！" + e.getMessage());
        }
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        //调用BrandService实现根据主键删除
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功！");
    }

    /***
     * 多条件搜索品牌数据
     * @param brand
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>> findList(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "查询成功！", list);
    }

    /***
     * Brand分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageResult<Brand>> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        //调用BrandService实现分页查询Brand
        PageResult<Brand> pageResult = brandService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "查询成功！", pageResult);
    }

    /***
     * 分页搜索实现
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageResult> findPage(@RequestBody(required = false) Brand brand,
                                       @PathVariable("page") int page,
                                       @PathVariable("size") int size) {
        //执行搜索
        PageResult<Brand> pageResult = brandService.findPage(brand, page, size);
        return new Result<>(true, StatusCode.OK, "查询成功！", pageResult);
    }

    //@ApiOperation(value = "查询品牌下拉列表", notes = "查询品牌下拉列表", tags = {"BrandController"})
    @GetMapping("/selectOptions")
    public List<Map> selectOptions() {
        return brandService.selectOptions();
    }
}
