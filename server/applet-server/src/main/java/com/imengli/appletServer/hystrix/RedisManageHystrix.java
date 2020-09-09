package com.imengli.appletServer.hystrix;

import com.imengli.appletServer.remote.RedisManageRemoter;
import org.springframework.stereotype.Component;

@Component
public class RedisManageHystrix implements RedisManageRemoter {

    // TODO: 没有想好怎么搞,待定吧。 By：2020-09-09 14:38:47

    @Override
    public Boolean set(String key, String value, Long time) {
        return null;
    }

    @Override
    public Boolean contains(String key) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Boolean getAndSet(String key, String value, Long time) {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return null;
    }
}
