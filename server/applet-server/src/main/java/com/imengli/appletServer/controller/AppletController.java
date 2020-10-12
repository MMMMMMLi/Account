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
