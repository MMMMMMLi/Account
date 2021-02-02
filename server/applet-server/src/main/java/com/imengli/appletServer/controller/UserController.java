/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
    public ResultDTO authUserInfo(@RequestParam String token,
                                  @RequestParam(value = "updateFlag", required = false, defaultValue = "true") Boolean updateFlag) {
        return userService.authUserInfo(token, updateFlag);
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
        return userService.updateWechatUserInfo(token, nickName, gender, city, province, country, avatarUrl, userInfoId);
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
                                    @RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "phone", required = false) String phone,
                                    @RequestParam(value = "banner", required = false, defaultValue = "false") Boolean banner) {
        return userService.updateUserInfo(token, address, name, phone, banner);

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
                        UUID.randomUUID().toString().replace("-", "")));
    }

    @RequestMapping("/addTempUserByApplet")
    public ResultDTO addTempUserByApplet(@RequestParam String token,
                                         @RequestParam String userName,
                                         @RequestParam String phoneNum,
                                         @RequestParam String avatarUrl) {
        return userService.addTempUserByApplet(token, userName, phoneNum, avatarUrl);
    }
}
