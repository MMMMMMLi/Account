package com.imengli.appletServer.service;

import com.alibaba.fastjson.JSON;
import com.imengli.appletServer.dao.UserRepostory;
import com.imengli.appletServer.daomain.UserEntity;
import com.imengli.appletServer.daomain.WechatAuthEntity;
import com.imengli.appletServer.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WechatAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(WechatAuthService.class);

    @Value("${applet.appID}")
    private String appID;

    @Value("${applet.appSecret}")
    private String appSecret;

    @Value("${applet.wechatAuthUri}")
    private String wechatAuthUri;

    // 存储OpenId
    // public static final InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    @Autowired
    private UserRepostory userRepostory;

    /**
     * 根据用户Code获取当前登陆用户信息
     *
     * @param code 用户登陆时候,微信给返回的唯一code码
     * @return 用户信息
     */
    public UserEntity codeToSession(String code) {
        UserEntity userEntity = new UserEntity();
        long lastLoginTimeStamp = System.currentTimeMillis();
        // 拼接url
        String authUrl = String.format(wechatAuthUri, appID, appSecret, code);
        // 调用微信的Auto接口,获取当前用户信息.
        String openid = null;
        try {
            openid = HttpUtil.getOpenid(authUrl);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return userEntity;
        }
        // 格式化为User对象
        WechatAuthEntity wechatAuthEntity = JSON.parseObject(openid, WechatAuthEntity.class);
        LOG.info(" >>>>>> wechatAuthEntity : {}", wechatAuthEntity);
        // 判断是否获取正常
        if (StringUtils.isAllEmpty(wechatAuthEntity.getErrcode(), wechatAuthEntity.getErrmsg())
                && StringUtils.isNoneEmpty(wechatAuthEntity.getOpenId(), wechatAuthEntity.getSessionKey())) {
            // 获取openId
            String openId = wechatAuthEntity.getOpenId();
            // 如果获取正常,先判断当前用户是否登陆过
            userEntity = userRepostory.getUserEntityByOpenId(openId);
            if (userEntity == null) {
                // 用户之前没有登陆过,则添加用户并继续获取用户详情信息
                userEntity = new UserEntity();
                userEntity.setOpenId(openId);
                userEntity.setUnionId(wechatAuthEntity.getUnionId());
                userEntity.setLastLoginTimeStamp(lastLoginTimeStamp);
                userEntity.setSessionKey(wechatAuthEntity.getSessionKey());
                userEntity.setSessionKeyTimeStamp(lastLoginTimeStamp);
                // 保存一下基本信息
                userRepostory.saveUserEntity(userEntity);
                // inheritableThreadLocal.set(openId);
                // new Thread(() -> {
                //    String openiD = inheritableThreadLocal.get();
                // }).start();
            } else {
                // 用户之前登陆过,则更新一下登陆时间
                UserEntity updateEntity = new UserEntity(userEntity.getOpenId());
                updateEntity.setLastLoginTimeStamp(lastLoginTimeStamp);
                // 如果Session_key相同,则不用更新
                if (!wechatAuthEntity.getSessionKey().equals(userEntity.getSessionKey())) {
                    // 如果不同,则更新一下
                    updateEntity.setSessionKey(wechatAuthEntity.getSessionKey());
                    updateEntity.setSessionKeyTimeStamp(lastLoginTimeStamp);
                }
                userRepostory.updateUserEntity(updateEntity);
            }
        }
        return userEntity;
    }
}
