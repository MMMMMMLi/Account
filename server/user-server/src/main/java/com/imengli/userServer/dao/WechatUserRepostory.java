package com.imengli.userServer.dao;

import com.imengli.userServer.daomain.WechatUserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from wechat_user where openId = #{openId}")
    WechatUserEntity getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update wechat_user " +
            "set unionId = #{wechatUserEntity.unionId} and  userId = #{wechatUserEntity.userId} " +
            "where openId = #{wechatUserEntity.openId}")
    void updateUserEntity(@Param("userEntity") WechatUserEntity wechatUserEntity);

}
