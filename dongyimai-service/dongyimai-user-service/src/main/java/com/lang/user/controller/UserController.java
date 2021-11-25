package com.lang.user.controller;

import com.alibaba.fastjson.JSON;
import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.user.pojo.User;
import com.lang.user.service.UserService;
import com.lang.util.BCrypt;
import com.lang.util.JwtUtil;
import com.lang.util.PhoneFormatCheckUtils;
import com.lang.util.TokenDecode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(tags = "UserController")
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/sendCode")
    public Result sendCode(String phone) {
        //判断手机号格式
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            return new Result(false, StatusCode.ERROR, "手机号格式不正确");
        }
        try {
            userService.createSmsCode(phone);//生成验证码
            return new Result(true, StatusCode.OK, "验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "验证码发送失败");
        }
    }

    /**
     * 增加
     *
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user, String smscode) {
        boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smscode);
        if (checkSmsCode == false) {
            return new Result(false, StatusCode.ERROR, "验证码输入错误！");
        }
        try {
            userService.add(user);
            return new Result(true, StatusCode.OK, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "增加失败");
        }
    }

    /***
     *用户登录
     */
    @RequestMapping(value = "/login")
    public Result login(String username, String password, HttpServletResponse response) {
        //查询用户信息
        User user = userService.findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            //设置令牌信息
            Map<String, Object> info = new HashMap<>();
            info.put("role", "USER");
            info.put("success", "SUCCESS");
            info.put("username", username);
            //生成令牌
            String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);
            //添加cookie
            Cookie cookie = new Cookie("Authorization", jwt);
            return new Result(true, StatusCode.OK, "登录成功！", user);
        }
        return new Result(false, StatusCode.LOGINERROR, "账号密码错误");
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Long id) {
        //调用UserService实现根据主键查询User
        User user = userService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询成功", user);
    }

    @GetMapping("/load/{username}")
    public Result<User> findByUsername(@PathVariable("username") String username) {
        //调用UserService实现根据主键查询User
        User user = userService.findByUsername(username);
        return new Result<>(true, StatusCode.OK, "查询成功", user);
    }

    /***
     * 增加用户积分
     * @param points:要添加的积分
     */
    @GetMapping(value = "/points/add")
    public Result addPoints(Integer points){
        //获取用户名
        Map<String, String> userMap = tokenDecode.getUserInfo();
        String username = userMap.get("user_name");

        //添加积分
        userService.addUserPoints(username,points);
        return new Result(true,StatusCode.OK,"添加积分成功！");
    }
}
