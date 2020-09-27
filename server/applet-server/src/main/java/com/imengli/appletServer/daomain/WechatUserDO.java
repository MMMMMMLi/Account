package com.imengli.appletServer.daomain;

/**
 * 用户实体类
 */
public class WechatUserDO {

    // 用户唯一ID
    private String openId;

    // 用户在开放平台的唯一标识符
    private String unionId;

    // 系统用户表Id
    private String userId;


    public WechatUserDO() {
    }

    public WechatUserDO(String openId, String unionId) {
        this.openId = openId;
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
