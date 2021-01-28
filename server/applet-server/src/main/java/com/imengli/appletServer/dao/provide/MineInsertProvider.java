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

package com.imengli.appletServer.dao.provide;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.daomain.OrderInfoDetailDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:30
 * @description: 自定义SQL插入
 * @version: v1
 */
public class MineInsertProvider {

    public String insertOrderInfoDetail(@Param("orders") List<OrderInfoDetailDO> orders) {
        return new SQL() {{
            INSERT_INTO(SysConstant.ORDER_INFO_DEATIL_TABLE_NAME);
            INTO_COLUMNS("id","orderId", "categoryValue", "sizeValue", "gross", "tare", "unitPrice", "totalPrice");
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = orders.size(); i < len; i++) {
                if (i > 0) {
                    sb.append(") , (");
                }
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].id}, ");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].orderId}, ");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].categoryValue},");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].sizeValue}, ");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].gross},");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].tare}, ");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].unitPrice}, ");
                sb.append("#{orders[");
                sb.append(i);
                sb.append("].totalPrice}");
            }
            INTO_VALUES(sb.toString());
        }}.toString();
    }

    public String insertSystemInfo(List<Map<String, Object>> insertInfos) {
        return new SQL() {{
            INSERT_INTO(SysConstant.CONSTANT_TABLE_NAME);
            INTO_COLUMNS("`key`", "`value`", "`type`", "createDate", "updateDate", "`status`");
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = insertInfos.size(); i < len; i++) {
                if (i > 0) {
                    sb.append(") , (");
                }
                sb.append("#{insertInfos[");
                sb.append(i);
                sb.append("].key}, ");
                sb.append("#{insertInfos[");
                sb.append(i);
                sb.append("].value}, ");
                sb.append("#{insertInfos[");
                sb.append(i);
                sb.append("].type}, ");
                sb.append("now(), now(), ");
                sb.append("#{insertInfos[");
                sb.append(i);
                sb.append("].status}");
            }
            INTO_VALUES(sb.toString());
        }}.toString();
    }
}
