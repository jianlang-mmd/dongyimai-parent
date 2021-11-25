package com.lang.itempage.feign;

import com.lang.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "dym-itempage")
public interface ItemPageFeign {

    @GetMapping("/page/createPageHtml/{spuId}")
    public Result createPageHtml(@PathVariable("spuId") Long spuId);

    @GetMapping("/page/deletePageHtml/{spuId}")
    public Result deletePageHtml(@PathVariable("spuId") Long spuId) ;
}
