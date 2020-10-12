package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.SysBannerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppletRepostory {

    @Select("SELECT *,CONCAT(picUrlPre,'/',picName) AS picUrl FROM sys_banners where status = 1 order by orderType")
    List<SysBannerDO> getBanners();
}
