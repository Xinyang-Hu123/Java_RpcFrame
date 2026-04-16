package com.xinyang.rpc.common.service;

import com.xinyang.rpc.common.pojo.User;

public interface Userservice {
    User getUserById(Integer id);
    Integer insertUserId(User user);

}
