package com.imengli.userServer.controller;

import com.imengli.userServer.dto.ResultDTO;
import com.imengli.userServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据Token值 校验用户信息
     *
     * @param token 自定义Token
     * @return 用户状态
     */
    @RequestMapping("/authUserInfo")
    public ResultDTO authUserInfo(@RequestParam String token) {
        return userService.authUserInfo(token);
    }

    @RequestMapping("/setUserInfo")
    public ResultDTO setUserInfo(@RequestParam String token,
                                 @RequestParam String nickName,
                                 @RequestParam Integer gender,
                                 @RequestParam String language,
                                 @RequestParam String city,
                                 @RequestParam String province,
                                 @RequestParam String country,
                                 @RequestParam String avatarUrl) {
        return userService.setUserInfo(token, nickName, gender, city, province, country, avatarUrl);
    }

}
