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
            WHERE("id = #{sysUserDO.id}" );
        }}.toString();

    }
}
