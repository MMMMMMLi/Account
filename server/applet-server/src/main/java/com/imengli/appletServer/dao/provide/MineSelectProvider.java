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
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/10 11:30
 * @description: 自定义SQL查询
 * @version: v1
 */
public class MineSelectProvider {

    public String selectOrderInfo(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  @Param("filterList") List<HashMap> filterList) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT " +
                "`order`.* " +
                "FROM " +
                SysConstant.ORDER_INFO_TABLE_NAME +
                " `order` ");
        if (filterList.size() > 0) {
            sb.append("LEFT JOIN " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " detail ON `order`.id = detail.orderId " +
                    "LEFT JOIN " + SysConstant.USER_TABLE_NAME + " `user` ON `order`.userId = `user`.id ");
        }
        sb.append("WHERE `order`.createDate BETWEEN #{startDate} AND #{endDate} ");
        if (filterList.size() > 0) {
            filterList.parallelStream()
                    // 去掉空Map
                    .filter(filter -> filter != null)
                    // 去掉搜索内容为空的数据
                    .filter(filter -> StringUtils.isNotBlank(filter.get("value").toString()))
                    .forEach(filter -> {
                        String[] keys = filter.get("key").toString().split(",");
                        if (keys.length > 1) {
                            sb.append("and ( ");
                            for (int i = 0; i < keys.length; i++) {
                                sb.append("LOCATE( '")
                                        .append(filter.get("value"))
                                        .append("',")
                                        .append(keys[i])
                                        .append(" )");
                                if (i != keys.length - 1) {
                                    sb.append(" or ");
                                }
                            }
                            sb.append(" )");
                        } else {
                            sb.append("and LOCATE( '")
                                    .append(filter.get("value"))
                                    .append("',")
                                    .append(filter.get("key"))
                                    .append(" )");
                        }

                    });
        }
        sb.append(" GROUP BY `order`.id ")
                .append("order by createDate desc");
        return sb.toString();
    }

    /**
     * 根据不同参数来获取用户列表
     *
     * @param searchMap
     * @return
     */
    public String getUserList(Map<String, String> searchMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        sb.append("	IFNULL(SUM(oi.totalPrice),0) AS money, ");
        sb.append("	COUNT(oi.totalPrice) AS orders, ");
        sb.append("	DATE_FORMAT(MAX(oi.createDate), '%m-%d %H:%i:%s') AS time, ");
        sb.append("	su.id as id, ");
        sb.append("	su.avatarUrl as avatarUrl, ");
        sb.append("	su.userName as userName, ");
        sb.append("	su.phoneNumber as phoneNumber, ");
        sb.append("	IF(su.isTemp = 0 ,'否', '是') as isTemp ");
        sb.append("FROM ");
        sb.append(SysConstant.USER_TABLE_NAME + " su ");
        sb.append("LEFT JOIN " + SysConstant.ORDER_INFO_TABLE_NAME + " oi ON su.id = oi.userId ");
        sb.append("WHERE 1 ");
        if (searchMap.containsKey("search")) {
            String searchValue = searchMap.get("search");
            sb.append(" AND (LOCATE('" + searchValue + "',userName) OR LOCATE('" + searchValue + "',userNameCode) OR LOCATE('" + searchValue + "',phoneNumber))");
        }
        if("isTemp".equals(searchMap.get("search")) || "isTemp".equals(searchMap.get("order"))) {
            sb.append(" AND su.isTemp = 1 ");
        }
        sb.append(" GROUP BY su.id ");
        if (searchMap.containsKey("order")) {
            sb.append(" ORDER BY " + searchMap.get("order") + " DESC");
        }

        return sb.toString();
    }
}
