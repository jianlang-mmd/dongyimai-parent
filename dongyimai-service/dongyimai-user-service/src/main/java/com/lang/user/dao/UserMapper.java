package com.lang.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lang.user.pojo.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

/****
 * @Author:ujiuye
 * @Description:User的Dao
 * @Date 2021/2/1 14:19
 *****/
public interface UserMapper extends BaseMapper<User> {
    /***
     * 增加用户积分
     * @param username
     * @param points
     * @return
     */
    @Update("UPDATE tb_user SET points=points+#{points} WHERE  username=#{username}")
    int addUserPoints(@Param("username") String username, @Param("point") Integer points);
}
