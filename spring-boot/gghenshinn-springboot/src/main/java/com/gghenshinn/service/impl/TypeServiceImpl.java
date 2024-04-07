package com.gghenshinn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gghenshinn.pojo.Type;
import com.gghenshinn.service.TypeService;
import com.gghenshinn.mapper.TypeMapper;
import com.gghenshinn.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author h1918
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-04-05 14:37:44
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Autowired
    private TypeMapper typeMapper;
    @Override
    public Result findAllTypes() {
        List<Type> types = typeMapper.selectList(null);
        return Result.ok(types);
    }
}




