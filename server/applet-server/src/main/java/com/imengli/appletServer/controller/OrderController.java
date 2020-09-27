package com.imengli.appletServer.controller;

import com.imengli.appletServer.dto.AddOrderFormInfoPOJO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/insertOrderInfo")
    public ResultDTO insertOrderInfo(@RequestBody() AddOrderFormInfoPOJO orderFormInfo) {
        return orderService.insertOrderInfo(orderFormInfo);
    }
}
