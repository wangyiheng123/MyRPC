package com.langchain.common.sevice;

import com.langchain.common.User;

public interface UserService {

    //根据ID查询用户
    User getUserByUserId(Integer id);
    //新增一个功能
    Integer insertUserId(User user);
}
