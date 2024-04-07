package com.gghenshinn.service;

import com.gghenshinn.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gghenshinn.utils.Result;

/**
* @author h1918
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-04-05 14:37:44
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
