package common.service;

import common.pojo.User;

public interface Userservice {
    User getUserById(Integer id);
    Integer insertUserId(User user);

}
