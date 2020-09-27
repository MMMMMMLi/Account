package com.imengli.appletServer.remote;

import com.imengli.appletServer.config.MineFeignConfig;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.remote.fallback.RedisWechatRemoteFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "redis-server", fallback = RedisWechatRemoteFallback.class, configuration = {MineFeignConfig.class})
public interface RedisWechatRemote {

    @RequestMapping("/wechat/set")
    public Boolean setWechat(@RequestParam(value = "key") String key, @RequestBody WechatAuthDO wechatAuthDO);

    @RequestMapping("/wechat/contains")
    public Boolean containsWechat(@RequestParam(value = "key") String key);

    @RequestMapping("/wechat/get")
    public WechatAuthDO getWechat(@RequestParam(value = "key") String key);

    @RequestMapping("/wechat/getAndSet")
    public Boolean getAndSetWechat(@RequestParam(value = "key") String key, @RequestBody WechatAuthDO wechatAuthDO);

    @RequestMapping("/wechat/delete")
    public Boolean deleteWechat(@RequestParam(value = "key") String key);

}
