package com.imengli.userServer.dao.provide;

import com.imengli.userServer.daomain.SysUserDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class MineUpdateProvider {

    private final String SYS_USER_TABLE_NAME = "sys_user";

    public String updateSysUserInfo(SysUserDO sysUserDO) {
        return new SQL(){{
            UPDATE(SYS_USER_TABLE_NAME);
            if (!StringUtils.isEmpty(sysUserDO.getUserName())) {
                SET("userName = #{sysUserEntity.userName}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getUserNameCode())) {
                SET("userNameCode= #{sysUserEntity.userNameCode}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getPhoneNumber())) {
                SET("phoneNumber= #{sysUserEntity.phoneNumber}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getAddress())) {
                SET("address= #{sysUserEntity.address}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getNickName())) {
                SET("nickName= #{sysUserEntity.nickName}");
            }
            if (!StringUtils.isEmpty(sysUserDO.getAvatarUrl())) {
                SET("avatarUrl= #{sysUserEntity.avatarUrl}");
            }
            if (sysUserDO.getLastLoginTimeStamp() != null) {
                SET("lastLoginTimeStamp= #{sysUserEntity.lastLoginTimeStamp}");
            }
            WHERE("id = #{sysUserEntity.id}" );
        }}.toString();

    }
}
