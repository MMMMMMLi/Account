package com.imengli.appletServer.controller;

import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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

    @RequestMapping("/updateUserInfo")
    public ResultDTO updateUserInfo(@RequestParam String token,
                                    @RequestParam String address,
                                    @RequestParam String name,
                                    @RequestParam String phone) {
        return userService.updateUserInfo(token, address, name, phone);

    }

    @RequestMapping("/getUserInfoBySearch")
    public ResultDTO getUserInfoBySearch(@RequestParam String token,
                                         @RequestParam String searchValue,
                                         @RequestParam Integer size) {
        return userService.getUserInfoBySearch(token,searchValue,size);
    }

}
