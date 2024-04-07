package com.gghenshinn.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gghenshinn.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gghenshinn.pojo.PortalVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author h1918
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-04-05 14:37:44
* @Entity com.gghenshinn.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

    IPage<Map> selectMyPage(IPage page, @Param("portalVo") PortalVo portalVo);

    Map selectDetailMap(Integer hid);
}




