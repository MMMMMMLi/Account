package com.imengli.appletServer.daomain;

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
public class SysUserDO {

    // 用户唯一ID
    private String id;

    // 用户姓名
    private String userName;

    // 用户拼音
    private String userNameCode;

    // 用户手机号
    private String phoneNumber;

    // 用户昵称
    private String nickName;

    // 用户头像Url
    private String avatarUrl;

    // 用户性别
    private int gender;

    // 用户所在国家
    private String country;

    // 用户所在省份
    private String province;

    // 用户所在城市
    private String city;

    // 用户地址
    private String address;

    // 最后一次登陆时间
    private Long lastLoginTimeStamp;

    public SysUserDO(String id, String userName, String userNameCode, String phoneNumber, String address) {
        this.id = id;
        this.userName = userName;
        this.userNameCode = userNameCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public SysUserDO(String id, String nickName, String avatarUrl, int gender, String country, String province, String city) {
        this.id = id;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.country = country;
        this.province = province;
        this.city = city;
    }

}
