package com.imengli.userServer.dao;

import com.imengli.userServer.daomain.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserRepostory {

    @Select("select * from sys_user where id = #{userId} limit 1")
    SysUserEntity getUserInfoById(@Param("userId") String userId);
}
