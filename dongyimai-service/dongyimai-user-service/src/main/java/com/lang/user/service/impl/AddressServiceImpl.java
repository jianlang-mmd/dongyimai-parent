package com.lang.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lang.user.dao.AddressMapper;
import com.lang.user.pojo.Address;
import com.lang.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /**
     * 根据用户查询地址
     *
     * @param userId
     * @return
     */
    @Override
    public List<Address> findListByUserId(String userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        //根据构建的条件查询数据
        return addressMapper.selectList(queryWrapper);
    }
}
