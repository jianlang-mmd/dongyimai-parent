package com.lang.itempage.controller;

import com.lang.itempage.service.PageService;
import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    PageService pageService;

    @GetMapping("/createPageHtml/{spuId}")
    public Result createPageHtml(@PathVariable("spuId") Long spuId){
        try {
            pageService.createPageHtml(spuId);
            return new Result(true,StatusCode.OK,"创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR,"创建失败");
        }
    }

    @GetMapping("/deletePageHtml/{spuId}")
    public Result deletePageHtml(@PathVariable("spuId") Long spuId) {
        pageService.deletePageHtml(spuId);
        return new Result(true,StatusCode.OK,"删除成功");
    }


}
