package com.imengli.appletServer.daomain;

/**
 * 微信接口返回的对象信息
 */
public class WechatAuthEntity {

    // 用户唯一ID
    private String openId;

    // 会话密钥
    private String session_key;

    // 用户在开放平台的唯一标识符
    private String unionId;

    // 出错状态码
    private String errcode;

    // 出错信息
    private String errmsg;

    public WechatAuthEntity() {
    }

    public WechatAuthEntity(String openId, String sessionKey, String unionId, String errcode, String errmsg) {
        this.openId = openId;
        this.session_key = sessionKey;
        this.unionId = unionId;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return session_key;
    }

    public void setSessionKey(String sessionKey) {
        this.session_key = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "WechatAuthEntity{" +
                "openId='" + openId + '\'' +
                ", sessionKey='" + session_key + '\'' +
                ", unionId='" + unionId + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
