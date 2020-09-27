package com.imengli.orderserver.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatUserDO {

    // 用户唯一ID
    private String openId;

    // 用户在开放平台的唯一标识符
    private String unionId;

    // 系统用户表Id
    private String userId;

    public WechatUserDO(String openId, String unionId) {
        this.openId = openId;
        this.unionId = unionId;
    }

}
