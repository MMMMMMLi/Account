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
import com.imengli.appletServer.daomain.WechatUserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from " + SysConstant.WECHAT_USER_TABLE_NAME + " where openId = #{openId}")
    WechatUserDO getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update " + SysConstant.WECHAT_USER_TABLE_NAME +
            " set unionId = #{wechatUserDO.unionId} , userId = #{wechatUserDO.userId} " +
            "where openId = #{wechatUserDO.openId}")
    void updateUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);

    @Insert("insert into " + SysConstant.WECHAT_USER_TABLE_NAME + " (openId,unionId) values(#{wechatUserDO.openId},#{wechatUserDO.unionId})")
    void saveUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);

    @Select("select * from " + SysConstant.WECHAT_USER_TABLE_NAME + " where userId = #{userId}")
    WechatUserDO getUserEntityByUserId(@Param("userId") String userId);
}
