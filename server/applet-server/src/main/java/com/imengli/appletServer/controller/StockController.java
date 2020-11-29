package com.imengli.appletServer.controller;

import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:13
 * @description: 库存管理相关操作
 * @version: v1
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping("/insertStock")
    public ResultDTO insertStock(@RequestParam Integer stockKey,
                                 @RequestParam String category,
                                 @RequestParam Double number,
                                 @RequestParam String token) {
        return stockService.insertStock(stockKey, category, number, token);
    }

    @RequestMapping("/getStockInfo")
    public ResultDTO getStockInfo(@RequestParam Integer stockKey,
                                  @RequestParam String token,
                                  @RequestParam Integer pageSize,
                                  @RequestParam Integer pageNum) {
        return stockService.getStockInfo(stockKey, token, pageNum, pageSize);
    }

    @RequestMapping("/alignStockInfo")
    public ResultDTO alignStockInfo(@RequestParam String jsonList,
                                  @RequestParam String token) {
        return stockService.alignStockInfo(jsonList,token);
    }
}
