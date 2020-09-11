package com.imengli.redisServer.controller;

import com.imengli.redisServer.daomain.WechatAuthEntity;
import com.imengli.redisServer.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/set")
    public Boolean set(@RequestParam(value = "key") String key, @RequestBody WechatAuthEntity wechatAuthEntity) {
        return redisService.setWechat(key, wechatAuthEntity);
    }


}
