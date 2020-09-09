package com.imengli.redisServer.controller;

import com.imengli.redisServer.service.RedisManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisManageController {

    @Autowired
    private RedisManageService redisManageService;

    @RequestMapping("/set")
    public Boolean set(@RequestParam String key, @RequestParam String value, @RequestParam(name = "time", required = false) Long time) {
        if (time == null) {
            return redisManageService.set(key, value);
        }
        return redisManageService.set(key, value, time);
    }

    @RequestMapping("/contains")
    public Boolean contains(@RequestParam String key) {
        return redisManageService.contains(key);
    }

    @RequestMapping("/get")
    public String get(@RequestParam String key) {
        return redisManageService.get(key);
    }

    @RequestMapping("/getAndSet")
    public Boolean getAndSet(@RequestParam String key, @RequestParam String value, @RequestParam(name = "time", required = false) Long time) {
        if (time == null) {
            return redisManageService.getAndSet(key, value);
        }
        return redisManageService.getAndSet(key, value, time);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestParam String key) {
        return redisManageService.delete(key);
    }
}
