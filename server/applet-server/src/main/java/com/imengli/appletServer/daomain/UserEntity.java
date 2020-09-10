package com.imengli.appletServer.daomain;

/**
 * 用户实体类
 */
public class UserEntity {

    // 用户唯一ID
    private String openId;

    // 用户在开放平台的唯一标识符
    private String unionId;

    // 用户手机号
    private String phoneNumber;

    // 用户姓名
    private String userName;

    // 用户拼音
    private String userNameCode;

    // 最后一次登陆时间
    private Long lastLoginTimeStamp;


    public UserEntity() {
    }

    public UserEntity(String openId) {
        this.openId = openId;
    }

    public UserEntity(String openId, Long lastLoginTimeStamp) {
        this.openId = openId;
        this.lastLoginTimeStamp = lastLoginTimeStamp;
    }

    public UserEntity(String openId, String unionId, Long lastLoginTimeStamp) {
        this.openId = openId;
        this.unionId = unionId;
        this.lastLoginTimeStamp = lastLoginTimeStamp;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameCode() {
        return userNameCode;
    }

    public void setUserNameCode(String userNameCode) {
        this.userNameCode = userNameCode;
    }

    public Long getLastLoginTimeStamp() {
        return lastLoginTimeStamp;
    }

    public void setLastLoginTimeStamp(Long lastLoginTimeStamp) {
        this.lastLoginTimeStamp = lastLoginTimeStamp;
    }

}