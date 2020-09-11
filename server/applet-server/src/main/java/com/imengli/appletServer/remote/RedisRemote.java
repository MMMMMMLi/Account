package com.imengli.appletServer.remote;

import com.imengli.appletServer.daomain.WechatAuthEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "redis-server")
public interface RedisRemote {

    @RequestMapping("/set")
    public Boolean set(@RequestParam(value = "key") String key, @RequestBody WechatAuthEntity wechatAuthEntity);
}
