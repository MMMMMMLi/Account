package com.imengli.appletServer.dao.provide;

import com.imengli.appletServer.daomain.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class MineUpdateProvider extends SQL {

    public String updateUserEntity(@Param("userEntity") UserEntity userEntity) {
        return new SQL() {{
            UPDATE("sys_user");
            if (StringUtils.isNotEmpty(userEntity.getUnionId())) {
                SET("unionId = #{userEntity.unionId}");
            }
            if (StringUtils.isNotEmpty(userEntity.getPhoneNumber())) {
                SET("phoneNumber= #{userEntity.phoneNumber}");
            }
            if (StringUtils.isNotEmpty(userEntity.getUserName())) {
                SET("userName= #{userEntity.userName}");
            }
            if (StringUtils.isNotEmpty(userEntity.getUserNameCode())) {
                SET("userNameCode= #{userEntity.userNameCode}");
            }
            if (userEntity.getLastLoginTimeStamp() != null) {
                SET("lastLoginTimeStamp= #{userEntity.lastLoginTimeStamp}");
            }
            WHERE("openId = #{userEntity.openId}");
        }}.toString();
    }
}
