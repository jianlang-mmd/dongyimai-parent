package com.lang.user.feign;

import com.lang.entity.Result;
import com.lang.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dym-user")
@RequestMapping("/user")
public interface UserFeign {

    /***
     * 根据username查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/load/{username}")
    public Result<User> findByUsername(@PathVariable("username") String username);

    /***
     * 添加用户积分
     * @param username
     * @return
     */
    @GetMapping("/addPoints")
    public Result addPoints(@RequestParam("username") String username);
}
