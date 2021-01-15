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

package com.imengli.appletServer.dao.provide;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.daomain.SysUserDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class MineUpdateProvider {

    public String updateSysUserInfo(SysUserDO sysUserDO) {
        return new SQL(){{
            UPDATE(SysConstant.USER_TABLE_NAME);
            if (!StringUtils.isEmpty(sysUserDO.getUserName())) {
                SET("userName = #{sysUserDO.userName}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getUserNameCode())) {
                SET("userNameCode= #{sysUserDO.userNameCode}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getPhoneNumber())) {
                SET("phoneNumber= #{sysUserDO.phoneNumber}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getAddress())) {
                SET("address= #{sysUserDO.address}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getNickName())) {
                SET("nickName= #{sysUserDO.nickName}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getAvatarUrl())) {
                SET("avatarUrl= #{sysUserDO.avatarUrl}");
            }
            if (sysUserDO.getCreateTime() != null) {
                SET("createTime = #{sysUserDO.createTime}");
            }
            if (sysUserDO.getUpdateTime() != null) {
                SET("updateTime= #{sysUserDO.updateTime}");
            }
            if (sysUserDO.getLastLoginTimeStamp() != null) {
                SET("lastLoginTimeStamp= #{sysUserDO.lastLoginTimeStamp}");
            }
            if (sysUserDO.getSubMsgNum() != null) {
                SET("subMsgNum= #{sysUserDO.subMsgNum}");
            }
            WHERE("id = #{sysUserDO.id}" );
        }}.toString();

    }
}
