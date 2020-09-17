package com.imengli.userServer.daomain;

/**
 * 用户实体类
 */
public class SysUserEntity {

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

    public SysUserEntity() {
    }

    public SysUserEntity(String id, String userName, String userNameCode, String phoneNumber, String address) {
        this.id = id;
        this.userName = userName;
        this.userNameCode = userNameCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public SysUserEntity(String id, String nickName, String avatarUrl, int gender, String country, String province, String city) {
        this.id = id;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getLastLoginTimeStamp() {
        return lastLoginTimeStamp;
    }

    public void setLastLoginTimeStamp(Long lastLoginTimeStamp) {
        this.lastLoginTimeStamp = lastLoginTimeStamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SysUserEntity{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", userNameCode='" + userNameCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", lastLoginTimeStamp=" + lastLoginTimeStamp +
                '}';
    }
}
