package com.lang.user.service;

import com.lang.user.pojo.Address;

import java.util.List;

public interface AddressService {
    /**
     * 根据用户查询地址
     *
     * @param userId
     * @return
     */
    List<Address> findListByUserId(String userId);
}
