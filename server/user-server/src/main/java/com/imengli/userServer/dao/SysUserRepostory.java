package com.imengli.userServer.dao;

import com.imengli.userServer.dao.provide.MineUpdateProvider;
import com.imengli.userServer.daomain.SysUserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserRepostory {

    @Select("select * from sys_user where id = #{userId} limit 1")
    SysUserEntity getUserInfoById(@Param("userId") String userId);

    @Insert("INSERT INTO " +
            "sys_user (id,nickName, avatarUrl, gender, country, province, city) " +
            "VALUES (#{sysUserEntity.id},#{sysUserEntity.nickName},#{sysUserEntity.avatarUrl},#{sysUserEntity.gender},#{sysUserEntity.country},#{sysUserEntity.province},#{sysUserEntity.city})")
    void save(@Param("sysUserEntity") SysUserEntity sysUserEntity);

    @UpdateProvider(type = MineUpdateProvider.class, method = "updateSysUserInfo")
    void update(@Param("sysUserEntity") SysUserEntity sysUserEntity);

    @Select("SELECT * FROM sys_user WHERE (LOCATE(#{searchValue},userName) OR LOCATE(#{searchValue},userNameCode) OR LOCATE(#{searchValue},phoneNumber)) LIMIT #{size}")
    List<SysUserEntity> getUserInfoBySearch(@Param("searchValue") String searchValue,@Param("size") Integer size);
}
