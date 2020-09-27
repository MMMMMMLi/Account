package com.imengli.redisServer.controller;

import com.imengli.redisServer.daomain.WechatAuthDO;
import com.imengli.redisServer.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class RedisWechatAuthController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/set")
    public Boolean setWechat(@RequestParam(value = "key") String key, @RequestBody WechatAuthDO wechatAuthDO) {
        return redisService.setWechat(key, wechatAuthDO);
    }

    @RequestMapping("/contains")
    public Boolean containsWechat(@RequestParam(value = "key") String key) {
        return redisService.containsWechat(key);
    }

    @RequestMapping("/get")
    public WechatAuthDO getWechat(@RequestParam(value = "key") String key) {
        return redisService.getWechat(key);
    }

    @RequestMapping("/getAndSet")
    public Boolean getAndSetWechat(@RequestParam(value = "key") String key, @RequestBody WechatAuthDO wechatAuthDO) {
        return redisService.getAndSetWechat(key, wechatAuthDO);
    }

    @RequestMapping("/delete")
    public Boolean deleteWechat(@RequestParam(value = "key") String key) {
        return redisService.deleteWechat(key);
    }
}
