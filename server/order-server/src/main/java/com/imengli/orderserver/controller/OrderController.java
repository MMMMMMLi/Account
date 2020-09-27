package com.imengli.orderserver.controller;

import com.imengli.orderserver.dto.AddOrderFormInfoPOJO;
import com.imengli.orderserver.dto.ResultDTO;
import com.imengli.orderserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/insertOrderInfo")
    public ResultDTO insertOrderInfo(@RequestBody() AddOrderFormInfoPOJO orderFormInfo) {
        return orderService.insertOrderInfo(orderFormInfo);
    }
}
