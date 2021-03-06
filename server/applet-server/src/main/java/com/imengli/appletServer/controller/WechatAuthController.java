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
