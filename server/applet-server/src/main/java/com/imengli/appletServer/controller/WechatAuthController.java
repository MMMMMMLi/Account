package com.imengli.appletServer.controller;

import com.imengli.appletServer.daomain.UserEntity;
import com.imengli.appletServer.service.WechatAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 用来请求微信服务器获取一些指定信息
 * Date: 2020-09-08 11:27:29
 * Author: Weijia Jiang
 */
@RestController
public class WechatAuthController {

    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping("/code2Session")
    public UserEntity codeToSession(@RequestParam String code) {
        return  wechatAuthService.codeToSession(code);
    }

}
