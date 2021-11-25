package com.lang.oauth.auth;

import com.lang.entity.Result;
import com.lang.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserFeign userFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用用户微服务，根据用户名获得用户信息
        Result<com.lang.user.pojo.User> userResult = userFeign.findByUsername(username);

        if (userResult != null && userResult.getData() != null) {
            //获取用户密码
            String pwd = userResult.getData().getPassword();
            //权限列表
            String permissions = "salesman,accountant,user";

            User userDetails =
                    new User(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
            return userDetails;
        } else {
            return null;
        }

//        // 一会登录的时候，密码必须写成dongyimai
//        String pwd = new BCryptPasswordEncoder().encode("dongyimai");
//
//        //创建角色（本应该从数据库中查询该用户的权限列表）
//        String permissions = "salesman,accountant,user";
//
//        //创建用户对象
//        User userDetails =
//                new User(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
//
//        return userDetails;
    }
}
