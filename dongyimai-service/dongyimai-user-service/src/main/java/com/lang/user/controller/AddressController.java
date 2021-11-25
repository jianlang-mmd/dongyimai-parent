package com.lang.user.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.user.pojo.Address;
import com.lang.user.service.AddressService;
import com.lang.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /****
     * 用户收件地址
     */
    @GetMapping(value = "/list/{userId}")
    public Result<List<Address>> findListByUserId(@PathVariable("userId") String userId) {
        //获取用户登录信息
        //Map<String, String> userMap = tokenDecode.getUserInfo();
        //String userId = userMap.get("user_name");
        //查询用户收件地址
        List<Address> addressList = addressService.findListByUserId(userId);
        return new Result(true, StatusCode.OK, "查询成功！", addressList);
    }
}
