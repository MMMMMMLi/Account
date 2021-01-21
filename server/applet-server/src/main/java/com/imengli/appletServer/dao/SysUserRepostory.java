/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
    @Result(property = "role", column = "roleId",
            one = @One(select = "com.imengli.appletServer.dao.SysRoleRepostory.selectRoleInfoById", fetchType = FetchType.EAGER))
    SysUserDO getUserInfoByUserId(@Param("userId") String userId);

    @Insert("INSERT INTO " + SysConstant.USER_TABLE_NAME +
            " (id,nickName, avatarUrl, gender, country, province, city, createTime, updateTime) " +
            "VALUES (#{sysUserEntity.id},#{sysUserEntity.nickName},#{sysUserEntity.avatarUrl},#{sysUserEntity.gender},#{sysUserEntity.country},#{sysUserEntity.province},#{sysUserEntity.city},#{sysUserEntity.createTime},#{sysUserEntity.updateTime})")
    void save(@Param("sysUserEntity") SysUserDO sysUserDO);

    @UpdateProvider(type = MineUpdateProvider.class, method = "updateSysUserInfo")
    void update(@Param("sysUserDO") SysUserDO sysUserDO);

    @Select("SELECT * FROM " + SysConstant.USER_TABLE_NAME +
            " WHERE (LOCATE(#{searchValue},userName) OR LOCATE(#{searchValue},userNameCode) OR LOCATE(#{searchValue},phoneNumber)) and state = 1 " +
            " LIMIT #{size}")
    List<SysUserDO> getUserInfoBySearch(@Param("searchValue") String searchValue, @Param("size") Integer size);

    @Insert("INSERT INTO " + SysConstant.USER_TABLE_NAME +
            " (id,userName, userNameCode, phoneNumber, avatarUrl, createTime, updateTime, isTemp) " +
            "VALUES (#{sysUserEntity.id},#{sysUserEntity.userName},#{sysUserEntity.userNameCode},#{sysUserEntity.phoneNumber},#{sysUserEntity.avatarUrl},#{sysUserEntity.createTime},#{sysUserEntity.createTime},#{sysUserEntity.isTemp})")
    void saveTempUser(@Param("sysUserEntity") SysUserDO build);
}
