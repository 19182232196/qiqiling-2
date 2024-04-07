package com.gghenshinn.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gghenshinn.pojo.User;
import com.gghenshinn.service.UserService;
import com.gghenshinn.mapper.UserMapper;
import com.gghenshinn.utils.JwtHelper;
import com.gghenshinn.utils.MD5Util;
import com.gghenshinn.utils.Result;
import com.gghenshinn.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author h1918
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-04-05 14:37:44
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private  UserMapper userMapper;

    /**
     * 登录业务实现
     * @param user
     * @return result封装
     */
    @Override
    public Result login(User user) {

        //根据账号查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(queryWrapper);

        //账号判断
        if (loginUser == null) {
            //账号错误
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //判断密码
        if (!StringUtils.isEmpty(user.getUserPwd())
                && loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd())))
        {
            //账号密码正确
            //根据用户唯一标识生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            Map data = new HashMap();
            data.put("token",token);

            return Result.ok(data);
        }

        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 查询用户数据
     * @param token
     * @return result封装
     */
    @Override
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询数据
        User user = userMapper.selectById(userId);

        if (user != null) {
            user.setUserPwd(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }

    /**
     * 检查账号是否可以注册
     *
     * @param username 账号信息
     * @return
     */
    @Override
    public Result checkUserName(String username) {

        // 使用LambdaQueryWrapper构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        // 根据查询条件查询用户信息
        User user = userMapper.selectOne(queryWrapper);

        // 判断用户信息是否存在
        if (user != null){
            // 返回用户名已被使用的结果
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        // 返回用户名可用的结果
        return Result.ok(null);
    }

    @Override
    public Result regist(User user) {
        //创建一个LambdaQueryWrapper实例
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个查询条件，表示用户名等于传入的用户名
        queryWrapper.eq(User::getUsername,user.getUsername());
        //调用userMapper的selectCount方法，查询满足条件的用户名数量
        Long count = userMapper.selectCount(queryWrapper);

        //如果满足条件的用户名数量大于0，则表示用户名已被使用
        if (count > 0){
            //返回一个错误结果，表示用户名已被使用
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        //调用MD5Util的encrypt方法，对用户密码进行加密
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        //调用userMapper的insert方法，插入用户信息
        int rows = userMapper.insert(user);
        //打印插入的行数
        System.out.println("rows = " + rows);
        //返回一个成功结果
        return Result.ok(null);
    }
}
