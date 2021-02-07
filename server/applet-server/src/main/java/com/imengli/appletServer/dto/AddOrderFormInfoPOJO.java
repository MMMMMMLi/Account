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

package com.imengli.appletServer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imengli.appletServer.daomain.OrderInfoDetailDO;
import com.imengli.appletServer.daomain.SysUserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 11:17
 * @description: 订单页面提交的Form表单内容
 * @version: v1
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderFormInfoPOJO implements Serializable {

    /**
     * form表单的标志,没用。
     */
    private String orderListId;

    /**
     * 订单Id。
     */
    private String orderInfoId;

    /**
     * 当前操作的用户Token
     */
    @JsonProperty(value = "token")
    private String token;

    /**
     * 用框数量
     */
    @JsonProperty(value = "applyBox")
    private Integer applyBox;

    /**
     * 退框数量
     */
    @JsonProperty(value = "retreatBox")
    private Integer retreatBox;

    /**
     * 当前订单金额
     */
    @JsonProperty(value = "totalPrice")
    private Double totalPrice;

    /**
     * 当前订单总重量
     */
    private Double totalWeight;

    /**
     * 当前订单生成时间
     */
    private String createDate;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 收款时间
     */
    private String collectionTime;

    /**
     * 用户信息
     */
    @JsonProperty(value = "userInfo")
    private SysUserDO userInfo;

    /**
     * 订单信息
     */
    @JsonProperty(value = "orders")
    private List<OrderInfoDetailDO> orders;

    /**
     * 当前订单是否已经通知
     */
    private Boolean isNotice;
}
