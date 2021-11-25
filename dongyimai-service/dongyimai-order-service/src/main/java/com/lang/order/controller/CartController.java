package com.lang.order.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.order.service.CartService;
import com.lang.order.util.Cart;
import com.lang.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(String username) {
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String userName = userInfo.get("user_name");
        return cartService.findCartListFromRedis(username);//从redis中提取
    }


    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        //String username = "ujiuye";
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("user_name");
        try {
            List<Cart> cartList = findCartList(username);//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            cartService.saveCartListToRedis(username, cartList);
            return new Result(true, StatusCode.OK, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加失败");
        }

    }

}
