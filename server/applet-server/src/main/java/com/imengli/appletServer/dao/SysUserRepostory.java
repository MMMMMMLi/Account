package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.provide.MineUpdateProvider;
import com.imengli.appletServer.daomain.SysUserDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SysUserRepostory {

    @Select("select * from " + SysConstant.USER_TABLE_NAME + " where id = #{userId} limit 1")
    @Result(property="role", column="roleId",
            one=@One(select="com.imengli.appletServer.dao.SysRoleRepostory.selectRoleInfoById", fetchType= FetchType.EAGER))
    SysUserDO getUserInfoById(@Param("userId") String userId);

    @Insert("INSERT INTO " + SysConstant.USER_TABLE_NAME +
            " (id,nickName, avatarUrl, gender, country, province, city, createTime, updateTime) " +
            "VALUES (#{sysUserEntity.id},#{sysUserEntity.nickName},#{sysUserEntity.avatarUrl},#{sysUserEntity.gender},#{sysUserEntity.country},#{sysUserEntity.province},#{sysUserEntity.city},#{sysUserEntity.createTime},#{sysUserEntity.updateTime})")
    void save(@Param("sysUserEntity") SysUserDO sysUserDO);

    @UpdateProvider(type = MineUpdateProvider.class, method = "updateSysUserInfo")
    void update(@Param("sysUserDO") SysUserDO sysUserDO);

    @Select("SELECT * FROM " + SysConstant.USER_TABLE_NAME + " WHERE (LOCATE(#{searchValue},userName) OR LOCATE(#{searchValue},userNameCode) OR LOCATE(#{searchValue},phoneNumber)) LIMIT #{size}")
    List<SysUserDO> getUserInfoBySearch(@Param("searchValue") String searchValue, @Param("size") Integer size);

    @Select("SELECT * FROM " + SysConstant.USER_TABLE_NAME + " WHERE id = #{id}")
    List<SysUserDO> getUserInfoByID(@Param("id") Integer id);
}
