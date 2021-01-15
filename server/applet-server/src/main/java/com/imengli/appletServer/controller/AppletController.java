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
import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.AppletRepostory;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.AppletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/12 8:56
 * @description: 小程序一些基本请求
 * @version: v1
 */

@RestController
@RequestMapping("/applet")
public class AppletController {

    @Autowired
    private AppletService appletService;

    @RequestMapping("/getBanners")
    public ResultDTO<List<AppletRepostory>> getBanners() {
        return appletService.getBanners();
    }

    @RequestMapping("/getConstant")
    public ResultDTO<Map> getConstant() {
        return new ResultDTO<>(ResultStatus.SUCCESS, SysConstant.getValuesByType("web"));
    }

}
