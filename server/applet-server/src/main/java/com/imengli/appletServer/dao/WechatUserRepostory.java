package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.daomain.WechatUserDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WechatUserRepostory {

    @Select("select * from " + SysConstant.WECHAT_USER_TABLE_NAME + " where openId = #{openId}")
    WechatUserDO getUserEntityByOpenId(@Param("openId") String openId);

    @Update("update " + SysConstant.WECHAT_USER_TABLE_NAME +
            " set unionId = #{wechatUserDO.unionId} , userId = #{wechatUserDO.userId} " +
            "where openId = #{wechatUserDO.openId}")
    void updateUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);

    @Insert("insert into " + SysConstant.WECHAT_USER_TABLE_NAME + " (openId,unionId) values(#{wechatUserDO.openId},#{wechatUserDO.unionId})")
    void saveUserEntity(@Param("wechatUserDO") WechatUserDO wechatUserDO);
}
