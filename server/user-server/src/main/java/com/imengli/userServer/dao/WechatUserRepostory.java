package com.imengli.userServer.dao;

import com.imengli.userServer.daomain.WechatUserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from wechat_user where openId = #{openId}")
    WechatUserDO getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update wechat_user " +
            "set unionId = #{wechatUserEntity.unionId} , userId = #{wechatUserEntity.userId} " +
            "where openId = #{wechatUserEntity.openId}")
    void updateUserEntity(@Param("wechatUserEntity") WechatUserDO wechatUserDO);
}
