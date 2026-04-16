package com.xinyang.rpc.common.impl;

import com.xinyang.rpc.common.pojo.User;
import com.xinyang.rpc.common.service.Userservice;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements Userservice {
    @Override
    public User getUserById(Integer id){
        System.out.println("客户端查询了" + id + "的用户");
        Random random = new Random();
        return User.builder()
                .userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean())
                .build();
    }
    @Override
    public Integer insertUserId(User user){
        System.out.println("插入成功" + user.getUserName());
        return user.getId();
    }

}
