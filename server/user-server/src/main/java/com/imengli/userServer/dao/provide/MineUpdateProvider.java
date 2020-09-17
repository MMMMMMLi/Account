package com.imengli.userServer.dao.provide;

import com.imengli.userServer.daomain.SysUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class MineUpdateProvider {

    private final String SYS_USER_TABLE_NAME = "sys_user";

    public String updateSysUserInfo(SysUserEntity sysUserEntity) {
        return new SQL(){{
            UPDATE(SYS_USER_TABLE_NAME);
            if (!StringUtils.isEmpty(sysUserEntity.getUserName())) {
                SET("userName = #{sysUserEntity.userName}");
            }
            if (!StringUtils.isEmpty(sysUserEntity.getUserNameCode())) {
                SET("userNameCode= #{sysUserEntity.userNameCode}");
            }
            if (!StringUtils.isEmpty(sysUserEntity.getPhoneNumber())) {
                SET("phoneNumber= #{sysUserEntity.phoneNumber}");
            }
            if (!StringUtils.isEmpty(sysUserEntity.getAddress())) {
                SET("address= #{sysUserEntity.address}");
            }
            if (!StringUtils.isEmpty(sysUserEntity.getNickName())) {
                SET("nickName= #{sysUserEntity.nickName}");
            }
            if (!StringUtils.isEmpty(sysUserEntity.getAvatarUrl())) {
                SET("avatarUrl= #{sysUserEntity.avatarUrl}");
            }
            if (sysUserEntity.getLastLoginTimeStamp() != null) {
                SET("lastLoginTimeStamp= #{sysUserEntity.lastLoginTimeStamp}");
            }
            WHERE("id = #{sysUserEntity.id}" );
        }}.toString();

    }
}
