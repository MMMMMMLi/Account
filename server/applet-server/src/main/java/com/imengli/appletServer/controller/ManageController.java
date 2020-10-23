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

    /**
     * 获取报告
     *
     * @param token
     * @param type  category | size | person | time
     * @param state 0 | 7 | 30
     * @return
     */
    @RequestMapping("/getReport")
    public ResultDTO getReport(@RequestParam String token,
                               @RequestParam String type,
                               @RequestParam Integer state) {
        return manageService.getReport(token, type, state);
    }
}
