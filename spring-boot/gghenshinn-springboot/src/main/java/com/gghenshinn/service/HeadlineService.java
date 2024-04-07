package com.gghenshinn.service;

import com.gghenshinn.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gghenshinn.pojo.PortalVo;
import com.gghenshinn.utils.Result;

/**
* @author h1918
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-04-05 14:37:44
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result publish(Headline headline,String token);

    Result findHeadlineByHid(Integer hid);

    Result updateHeadLine(Headline headline);
}
