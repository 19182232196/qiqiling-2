package com.gghenshinn.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gghenshinn.pojo.Headline;
import com.gghenshinn.pojo.PortalVo;
import com.gghenshinn.service.HeadlineService;
import com.gghenshinn.mapper.HeadlineMapper;
import com.gghenshinn.utils.JwtHelper;
import com.gghenshinn.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author h1918
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-04-05 14:37:44
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;
    @Autowired
    private JwtHelper jwtHelper;

    @Override
    // 根据传入的参数查找新页面
    public Result findNewPage(PortalVo portalVo) {
        // 创建一个分页对象
        IPage<Map> page = new Page<>(
                portalVo.getPageNum(),portalVo.getPageSize());
        // 调用headlineMapper的selectMyPage方法，查询分页数据
        headlineMapper.selectMyPage(page,portalVo);

        // 获取分页后的数据
        List<Map> records= page.getRecords();
        // 创建一个HashMap，用于存放分页数据
        Map data = new HashMap();
        data.put("pageData",records);
        // 获取当前页码
        data.put("pageNum",page.getCurrent());
        // 获取每页显示的记录数
        data.put("pageSize",page.getSize());
        // 获取总页数
        data.put("totalPage",page.getPages());
        // 获取总记录数
        data.put("totalSize",page.getTotal());

        // 创建一个HashMap，用于存放分页信息和数据
        Map pageInfo = new HashMap();
        pageInfo.put("pageInfo",data);

        // 返回结果
        return Result.ok(pageInfo);
    }

    @Override
    public Result showHeadlineDetail(Integer hid) {

        //1.实现根据id的查询(多表
        Map headLineDetail = headlineMapper.selectDetailMap(hid);
        //2.拼接头条对象(阅读量和version)进行数据更新
        Headline headline = new Headline();
        headline.setHid(hid);
        headline.setPageViews((Integer) headLineDetail.get("pageViews")+1); //阅读量+1
        headline.setVersion((Integer) headLineDetail.get("version")); //设置版本
        headlineMapper.updateById(headline);

        Map<String,Object> pageInfoMap=new HashMap<>();
        pageInfoMap.put("headline",headLineDetail);
        return Result.ok(pageInfoMap);
    }

    /**
     * 发布数据
     * @param headline
     * @return
     */
    @Override
    public Result publish(Headline headline,String token) {
        // 使用JWT工具类获取用户ID
        int userId = jwtHelper.getUserId(token).intValue();
        // 将标题添加到数据库
        headline.setPublisher(userId);
        // 设置页面访问次数为0
        headline.setPageViews(0);
        // 设置创建时间和更新时间
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        // 调用插入方法将标题添加到数据库
        headlineMapper.insert(headline);

        // 返回成功结果
        return Result.ok(null);
    }

    /**
     * 根据id查询详情
     * @param hid
     * @return
     */
    // 根据hid查找 headline
    @Override
    public Result findHeadlineByHid(Integer hid) {
        // 调用 headlineMapper 查询指定hid的 headline
        Headline headline = headlineMapper.selectById(hid);
        // 创建一个Map用于存放查询到的 headline
        Map<String,Object> pageInfoMap=new HashMap<>();
        // 将 headline 放入Map中
        pageInfoMap.put("headline",headline);
        // 返回包含 headline 的 Result 对象
        return Result.ok(pageInfoMap);
    }

    /**
     * 修改业务
     * 1.查询version版本
     * 2.补全属性,修改时间 , 版本!
     *
     * @param headline
     * @return
     */
    @Override
    //根据id更新标题
    public Result updateHeadLine(Headline headline) {

        //读取版本
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();

        //设置版本号
        headline.setVersion(version);
        //设置更新时间
        headline.setUpdateTime(new Date());

        //更新标题
        headlineMapper.updateById(headline);

        //返回结果
        return Result.ok(null);
    }

}




