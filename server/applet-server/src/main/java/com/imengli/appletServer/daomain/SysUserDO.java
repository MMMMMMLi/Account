/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    // 最后一次登陆时间
    private Long lastLoginTimeStamp;

    // 用户消息通知的次数
    private Integer subMsgNum;

    // 权限
    private SysRoleDo role;

    // 权限
    private Integer state;

    // 是否是测试用户
    private Boolean isTemp;

    public SysUserDO(String id, String userName, String userNameCode, String phoneNumber, String address, LocalDateTime updateTime) {
        this.id = id;
        this.userName = userName;
        this.userNameCode = userNameCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.updateTime = updateTime;
    }

    public SysUserDO(String id, String nickName, String avatarUrl, int gender, String country, String province, String city, LocalDateTime createTime, LocalDateTime updateTime) {
        this(id,nickName,avatarUrl,gender,country,province,city,createTime,updateTime,0);
    }

    public SysUserDO(String id, String nickName, String avatarUrl, int gender, String country, String province, String city, LocalDateTime createTime, LocalDateTime updateTime,Integer subMsgNum ) {
        this.id = id;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.country = country;
        this.province = province;
        this.city = city;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.subMsgNum = subMsgNum;
    }

}
