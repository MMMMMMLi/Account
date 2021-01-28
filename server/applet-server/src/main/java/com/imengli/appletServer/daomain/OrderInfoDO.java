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

package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 15:11
 * @description: 订单列表信息
 * @version: v1
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDO {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建者id
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 创建时间
     */
    private LocalDateTime updateDate;

    /**
     * 使用框子数量
     */
    private Integer applyBox;

    /**
     * 退框数量
     */
    private Integer retreatBox;

    /**
     * 总价钱
     */
    private Double totalPrice;

    /**
     * 当前订单总重量
     */
    private Double totalWeight;

    /**
     * 订单状态
     */
    private int status;

    /**
     * 收款时间
     */
    private LocalDateTime collectionTime;

    /**
     * 是否通知
     */
    private Boolean isNotice;
}
