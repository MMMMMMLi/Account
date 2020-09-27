package com.imengli.appletServer.remote.fallback;

import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.remote.RedisWechatRemote;

public class RedisWechatRemoteFallback implements RedisWechatRemote {

    // TODO: 没有想好怎么搞,后续再说, by: 2020-09-11 10:37:25

    @Override
    public Boolean setWechat(String key, WechatAuthDO wechatAuthDO) {
        return null;
    }

    @Override
    public Boolean containsWechat(String key) {
        return null;
    }

    @Override
    public WechatAuthDO getWechat(String key) {
        return null;
    }

    @Override
    public Boolean getAndSetWechat(String key, WechatAuthDO wechatAuthDO) {
        return null;
    }

    @Override
    public Boolean deleteWechat(String key) {
        return null;
    }
}
