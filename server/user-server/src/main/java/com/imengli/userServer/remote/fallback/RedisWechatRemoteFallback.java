package com.imengli.userServer.remote.fallback;


import com.imengli.userServer.daomain.WechatAuthEntity;
import com.imengli.userServer.remote.RedisWechatRemote;

public class RedisWechatRemoteFallback implements RedisWechatRemote {

    // TODO: 没有想好怎么搞,后续再说, by: 2020-09-11 10:37:25

    @Override
    public Boolean setWechat(String key, WechatAuthEntity wechatAuthEntity) {
        return null;
    }

    @Override
    public Boolean containsWechat(String key) {
        return null;
    }

    @Override
    public WechatAuthEntity getWechat(String key) {
        return null;
    }

    @Override
    public Boolean getAndSetWechat(String key, WechatAuthEntity wechatAuthEntity) {
        return null;
    }

    @Override
    public Boolean deleteWechat(String key) {
        return null;
    }
}
