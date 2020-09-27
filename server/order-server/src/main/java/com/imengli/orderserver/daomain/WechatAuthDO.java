package com.imengli.orderserver.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信接口返回的对象信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatAuthDO implements Serializable {

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
}
