package com.lang.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lang.entity.PageResult;
import com.lang.user.pojo.User;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:User业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface UserService extends IService<User> {

    public User findByUsername(String username);

    /**
     * 创建随机验证码，存储到redis中，hash（smsHash ）  phone-code
     * @param phone
     */
    public void createSmsCode(String phone);

    /**
     *
     * @param mobile
     * @param code 注册页面输入的验证码
     */
    public boolean checkSmsCode(String mobile ,String code);

    /***
     * User多条件分页查询
     * @param user
     * @param page
     * @param size
     * @return
     */
    PageResult<User> findPage(User user, int page, int size);

    /***
     * User分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<User> findPage(int page, int size);

    /***
     * User多条件搜索方法
     * @param user
     * @return
     */
    List<User> findList(User user);

    /***
     * 删除User
     * @param id
     */
    void delete(Long id);

    /***
     * 修改User数据
     * @param user
     */
    void update(User user);

    /***
     * 新增User
     * @param user
     */
    void add(User user);

    /**
     * 根据ID查询User
     * @param id
     * @return
     */
     User findById(Long id);

    /***
     * 查询所有User
     * @return
     */
    List<User> findAll();

    /***
     * 添加用户积分
     * @param username
     * @param point
     * @return
     */
    int addUserPoints(String username,Integer point);
}
