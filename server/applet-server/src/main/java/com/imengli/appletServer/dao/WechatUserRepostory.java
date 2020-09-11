package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.WechatUserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from wechat_user where openId = #{openId}")
    WechatUserEntity getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update wechat_user " +
            "set unionId = #{wechatUserEntity.unionId} and userId = #{wechatUserEntity.userId} " +
            "where openId = #{wechatUserEntity.openId}")
    void updateUserEntity(@Param("wechatUserEntity") WechatUserEntity wechatUserEntity);

    @Insert("insert into wechat_user (openId,unionId) values(#{wechatUserEntity.openId},#{wechatUserEntity.unionId})")
    void saveUserEntity(@Param("wechatUserEntity") WechatUserEntity wechatUserEntity);
}
