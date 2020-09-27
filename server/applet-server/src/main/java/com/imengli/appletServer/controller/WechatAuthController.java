package com.imengli.appletServer.controller;

import com.imengli.appletServer.dto.ResultDTO;
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
@RequestMapping("/applet/auth")
public class WechatAuthController {

    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping("/code2Session")
    public ResultDTO<String> codeToSession(@RequestParam String code,@RequestParam(required = false) String token) {
        return wechatAuthService.codeToSession(code,token);
    }

    @RequestMapping("/checkToken")
    public ResultDTO<String> checkToken(@RequestParam String token) {
        return wechatAuthService.checkToken(token);
    }


}
