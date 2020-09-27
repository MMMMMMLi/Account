package com.imengli.appletServer.dao;

import com.imengli.appletServer.dao.provide.MineUpdateProvider;
import com.imengli.appletServer.daomain.SysUserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserRepostory {

    @Select("select * from sys_user where id = #{userId} limit 1")
    SysUserDO getUserInfoById(@Param("userId") String userId);

    @Insert("INSERT INTO " +
            "sys_user (id,nickName, avatarUrl, gender, country, province, city) " +
            "VALUES (#{sysUserEntity.id},#{sysUserEntity.nickName},#{sysUserEntity.avatarUrl},#{sysUserEntity.gender},#{sysUserEntity.country},#{sysUserEntity.province},#{sysUserEntity.city})")
    void save(@Param("sysUserEntity") SysUserDO sysUserDO);

    @UpdateProvider(type = MineUpdateProvider.class, method = "updateSysUserInfo")
    void update(@Param("sysUserEntity") SysUserDO sysUserDO);

    @Select("SELECT * FROM sys_user WHERE (LOCATE(#{searchValue},userName) OR LOCATE(#{searchValue},userNameCode) OR LOCATE(#{searchValue},phoneNumber)) LIMIT #{size}")
    List<SysUserDO> getUserInfoBySearch(@Param("searchValue") String searchValue, @Param("size") Integer size);
}
