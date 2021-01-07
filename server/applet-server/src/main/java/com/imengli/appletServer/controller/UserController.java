package com.imengli.appletServer.controller;

import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    /**
     * 设置用户微信的基本信息
     *
     * @param token
     * @param nickName
     * @param gender
     * @param language
     * @param city
     * @param province
     * @param country
     * @param avatarUrl
     * @return
     */
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

    /**
     * 更新用户微信的基本信息
     *
     * @param token
     * @param nickName
     * @param gender
     * @param language
     * @param city
     * @param province
     * @param country
     * @param avatarUrl
     * @param userInfoId
     * @return
     */
    @RequestMapping("/updateWechatUserInfo")
    public ResultDTO updateWechatUserInfo(@RequestParam String token,
                                          @RequestParam String nickName,
                                          @RequestParam Integer gender,
                                          @RequestParam String language,
                                          @RequestParam String city,
                                          @RequestParam String province,
                                          @RequestParam String country,
                                          @RequestParam String avatarUrl,
                                          @RequestParam String userInfoId) {
        return userService.updateWechatUserInfo(token, nickName, gender, city, province, country, avatarUrl,userInfoId);
    }

    /**
     * 更新用户的个人信息
     *
     * @param token
     * @param address
     * @param name
     * @param phone
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public ResultDTO updateUserInfo(@RequestParam String token,
                                    @RequestParam String address,
                                    @RequestParam String name,
                                    @RequestParam String phone) {
        return userService.updateUserInfo(token, address, name, phone);

    }

    /**
     * 获取用户信息
     *
     * @param token
     * @param searchValue
     * @param size
     * @return
     */
    @RequestMapping("/getUserInfoBySearch")
    public ResultDTO getUserInfoBySearch(@RequestParam String token,
                                         @RequestParam String searchValue,
                                         @RequestParam Integer size) {
        return userService.getUserInfoBySearch(token, searchValue, size);
    }

    /**
     * 修改用户的消息提示次数
     *
     * @param userId
     * @return
     */
    @RequestMapping("/userSubMsgAdd")
    public ResultDTO userSubMsgAdd(@RequestParam String userId) {
        return userService.userSubMsgAdd(userId);
    }

    /**
     * 以下内容更新于：2021-01-07 14:39:06
     *
     * 主要是增加了管理员可以通过页面添加用户，方便管理员可以操作一些没有使用过本系统的用户的订单。
     * 但是增加以下逻辑之后，还需要增加用户关联的操作。
     *
     */

    /**
     * 生成随机头像
     */
    @RequestMapping("/refreshAva")
    public ResultDTO refreshAva() {
        return new ResultDTO(ResultStatus.SUCCESS,
                String.format("https://gravatar.loli.net/avatar/%s?d=monsterid&v=1.4.14",
                        UUID.randomUUID().toString().replace("-","")));
    }

    @RequestMapping("/addUserByApplet")
    public ResultDTO addUser() {
        return  null;
    }
}
