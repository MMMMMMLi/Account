package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.BannerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BannerRepostory {

    @Select("SELECT id,CONCAT(picUrlPre,'/',picName) AS picUrl,`status` FROM sys_banners where status = 1")
    List<BannerEntity> getBanners();
}
