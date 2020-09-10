package com.imengli.appletServer.daomain;

import java.io.Serializable;

/**
 * 微信接口返回的对象信息
 */
public class WechatAuthEntity implements Serializable {

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
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
                ", session_key='" + session_key + '\'' +
                ", unionId='" + unionId + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
