package com.lang.search.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 导入数据
     *
     * @return
     */
    @GetMapping("/import")
    public Result importSku() {
        skuService.importSku();
        return new Result(true, StatusCode.OK, "导入数据到索引库中成功！");
    }

    /**
     * 搜索
     *
     * @param searchMap
     * @return
     */
    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap) {
        return skuService.search(searchMap);
    }

    @DeleteMapping
    public Result clearAll() {
        try {
            skuService.clearAll();
            return new Result(true, StatusCode.OK, "清空成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "清空失败");
        }
    }

}
