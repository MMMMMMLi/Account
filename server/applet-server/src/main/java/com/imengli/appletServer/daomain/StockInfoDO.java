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
 * @date: Created in 2020/11/26 11:39
 * @description: 库存信息
 * @version: v1
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInfoDO {

    /**
     * 标识
     */
    private Integer key;

    /**
     * 品种
     */
    private String category;

    /**
     * 大小
     */
    private String size;

    /**
     * 数量
     */
    private Double number;

    /**
     * 创建时间
     */
    private LocalDateTime updateDate;

    /**
     * 创建人
     */
    private String updateBy;
}
