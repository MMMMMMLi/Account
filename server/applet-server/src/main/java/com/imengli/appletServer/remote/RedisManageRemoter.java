package com.imengli.appletServer.remote;

import com.imengli.appletServer.config.MineFeignConfig;
import com.imengli.appletServer.hystrix.RedisManageHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "redis-server", fallback = RedisManageHystrix.class, configuration = {MineFeignConfig.class})
public interface RedisManageRemoter {

    @RequestMapping("/set")
    public Boolean set(@RequestParam String key, @RequestParam String value, @RequestParam(name = "time", required = false) Long time);

    @RequestMapping("/contains")
    public Boolean contains(@RequestParam String key);

    @RequestMapping("/get")
    public String get(@RequestParam String key);

    @RequestMapping("/getAndSet")
    public Boolean getAndSet(@RequestParam String key, @RequestParam String value, @RequestParam(name = "time", required = false) Long time);

    @RequestMapping("/delete")
    public Boolean delete(@RequestParam String key);
}
