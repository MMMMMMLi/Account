package com.imengli.appletServer.controller;

import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/22 15:25
 * @description: 管理相关Controller
 * @version: v1
 */
@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;

    // ----------------------------------------订单信息----------------------------------------------------

    /**
     * 获取 订单汇总信息
     *
     * @param token
     * @return
     */
    @RequestMapping("/getSummaryOrderInfo")
    public ResultDTO getSummaryOrderInfo(@RequestParam String token) {
        return manageService.getSummaryOrderInfo(token);
    }

    // ----------------------------------------报告系列----------------------------------------------------

    /**
     * 获取每日报告
     *
     * @param token
     * @param type  category | size | person | time
     * @return
     */
    @RequestMapping("/getReportByDay")
    public ResultDTO getReportByDay(@RequestParam String token,
                                    @RequestParam String type) {
        return manageService.getReportByDay(token, type);
    }

    /**
     * 获取每周报告
     *
     * @param token
     * @param type  category | size | person | order
     * @return
     */
    @RequestMapping("/getReportByWeek")
    public ResultDTO getReportByWeek(@RequestParam String token,
                                     @RequestParam String type) {
        return manageService.getReportByDay(token, type);
    }


    // ----------------------------------------客户管理----------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param token
     * @param searchType
     * @param searchValue
     * @return
     */
    @RequestMapping("/getUserList")
    public ResultDTO getUserList(@RequestParam String token,
                                 @RequestParam Integer page,
                                 @RequestParam Integer size,
                                 @RequestParam(value = "searchType", required = false) String searchType,
                                 @RequestParam(value = "searchValue", required = false) String searchValue) {
        return manageService.getUserList(token, page, size, searchType, searchValue);
    }


    @RequestMapping("/getUserDetails")
    public ResultDTO getUserDetails(@RequestParam String token,
                                    @RequestParam String userId,
                                    @RequestParam Integer page,
                                    @RequestParam Integer size) {
        return manageService.getUserDetails(token, userId, page, size);
    }


    // ----------------------------------------系统管理----------------------------------------------------

    /**
     * 获取系统管理数据
     *
     * @param token
     * @return
     */
    @RequestMapping("/getSysInfo")
    public ResultDTO getSysInfo(@RequestParam String token) {
        return manageService.getSysInfo(token);
    }

    @RequestMapping("/updateSystemInfo")
    public ResultDTO updateSystemInfo(@RequestParam String sysInfo) {
        return manageService.updateSystemInfo(sysInfo);
    }

    // ----------------------------------------订单分析----------------------------------------------------
    // ----------------------------------------告警信息----------------------------------------------------
    /**
     * 获取告警列表
     *
     * @param token
     * @return
     */
    @RequestMapping("/getWarnList")
    public ResultDTO getWarnList(@RequestParam String token,
                                 @RequestParam Integer page,
                                 @RequestParam Integer size,
                                 @RequestParam(value = "value", required = false) Integer value) {
        return manageService.getWarnList(token, page, size, value);
    }
}
