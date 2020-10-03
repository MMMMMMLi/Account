package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.WechatUserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from wechat_user where openId = #{openId}")
    WechatUserDO getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update wechat_user " +
            "set unionId = #{wechatUserDO.unionId} , userId = #{wechatUserDO.userId} " +
            "where openId = #{wechatUserDO.openId}")
    void updateUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);

    @Insert("insert into wechat_user (openId,unionId) values(#{wechatUserDO.openId},#{wechatUserDO.unionId})")
    void saveUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);
}
