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

import com.imengli.appletServer.dto.AddOrderFormInfoPOJO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/getMyOrderList")
    public ResultDTO getMyOrderList(@RequestParam String token,
                                    @RequestParam Integer status,
                                    @RequestParam Integer page,
                                    @RequestParam Integer size) {
        return orderService.getMyOrderList(token, status, page, size);
    }

    @RequestMapping("/getOrderList")
    public ResultDTO getOrderList(@RequestParam String token,
                                  @RequestParam Integer status,
                                  @RequestParam Integer page,
                                  @RequestParam Integer size,
                                  @RequestParam String filterList) {
        return orderService.getOrderList(token, status, page, size, filterList);
    }

    @RequestMapping("/confirmCollection")
    public ResultDTO confirmCollection(@RequestParam String token,
                                       @RequestParam Long orderId) {
        return orderService.confirmCollection(token, orderId);
    }

    @RequestMapping("/getUserOrderSize")
    public ResultDTO getUserOrderSize(@RequestParam String userId) {
        return orderService.getUserOrderSize(userId);
    }

    @RequestMapping("/sendMsg")
    public ResultDTO sendMsg(
            @RequestParam Boolean flag,
            @RequestParam String id) {
        return orderService.sendMsg(flag,id);
    }
}
