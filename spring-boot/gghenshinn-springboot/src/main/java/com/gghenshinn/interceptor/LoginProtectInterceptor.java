package com.gghenshinn.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gghenshinn.utils.JwtHelper;
import com.gghenshinn.utils.Result;
import com.gghenshinn.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器，用于拦截用户请求
 */
@Component
public class LoginProtectInterceptor implements HandlerInterceptor {

    /**
     * JWT工具类
     */
    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 重写HandlerInterceptor的preHandle方法
     * 请求处理之前调用的方法，用于检查用户是否已经登录
     * 如果用户未登录，则返回登录失败的结果
     * 如果用户已登录，则返回继续执行的结果
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从请求头中获取token
        String token = request.getHeader("token");
        // 判断token是否为空或者是否过期
        if (StringUtils.isEmpty(token) || jwtHelper.isExpiration(token)){
            // 如果token为空或者过期，则返回登录失败的结果
            Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
            // 创建JSON序列化器
            ObjectMapper objectMapper = new ObjectMapper();
            // 将结果转换为JSON字符串
            String json = objectMapper.writeValueAsString(result);
            // 将JSON字符串写入响应中
            response.getWriter().print(json);
            // 拦截
            return false;
        }else{
            // 如果token有效，则放行
            return true;
        }
    }
}

