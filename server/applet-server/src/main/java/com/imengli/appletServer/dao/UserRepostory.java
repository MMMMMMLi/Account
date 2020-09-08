package com.imengli.appletServer.dao;

import com.imengli.appletServer.dao.provide.MineUpdateProvider;
import com.imengli.appletServer.daomain.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserRepostory {

    @Select("select * from sys_user where openId = #{openId}")
    UserEntity getUserEntityByOpenId(@Param("openId") String openId);

    @UpdateProvider(type = MineUpdateProvider.class, method = "updateUserEntity")
    void updateUserEntity(@Param("userEntity") UserEntity userEntity);

    @Insert("insert into sys_user " +
            "(openId,unionId,lastLoginTimeStamp,sessionKey,sessionKeyTimeStamp) " +
            "values " +
            "(#{userEntity.openId},#{userEntity.unionId},#{userEntity.lastLoginTimeStamp},#{userEntity.sessionKey},#{userEntity.sessionKeyTimeStamp})")
    void saveUserEntity(@Param("userEntity") UserEntity userEntity);
}
