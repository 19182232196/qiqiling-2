package com.gghenshinn.service;

import com.gghenshinn.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gghenshinn.utils.Result;

/**
* @author h1918
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-04-05 14:37:44
*/
public interface TypeService extends IService<Type> {

    Result findAllTypes();
}
