package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.WechatUserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from wechat_user where openId = #{openId}")
    WechatUserDO getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update wechat_user " +
            "set unionId = #{wechatUserEntity.unionId} and userId = #{wechatUserEntity.userId} " +
            "where openId = #{wechatUserEntity.openId}")
    void updateUserEntity(@Param("wechatUserEntity") WechatUserDO wechatUserDO);

    @Insert("insert into wechat_user (openId,unionId) values(#{wechatUserEntity.openId},#{wechatUserEntity.unionId})")
    void saveUserEntity(@Param("wechatUserEntity") WechatUserDO wechatUserDO);
}
