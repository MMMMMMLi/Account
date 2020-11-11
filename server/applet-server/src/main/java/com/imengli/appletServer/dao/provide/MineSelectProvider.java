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
                " `order` " +
                "LEFT JOIN " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " detail ON `order`.id = detail.orderId " +
                "LEFT JOIN " + SysConstant.USER_TABLE_NAME + " `user` ON `order`.userId = `user`.id " +
                "WHERE createDate BETWEEN #{startDate} AND #{endDate} ");
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
        sb.append("	SUM(oi.totalPrice) AS money, ");
        sb.append("	COUNT(oi.totalPrice) AS orders, ");
        sb.append("	DATE_FORMAT(MAX(oi.createDate), '%m-%d %H:%i:%s') AS time, ");
        sb.append("	su.* ");
        sb.append("FROM ");
        sb.append(SysConstant.USER_TABLE_NAME + " su ");
        sb.append("LEFT JOIN " + SysConstant.ORDER_INFO_TABLE_NAME + " oi ON su.id = oi.userId ");
        if (searchMap.containsKey("search")) {
            sb.append("WHERE ");
            String searchValue = searchMap.get("search");
            sb.append("(LOCATE('" + searchValue + "',userName) OR LOCATE('" + searchValue + "',userNameCode) OR LOCATE('" + searchValue + "',phoneNumber))");
        }
        sb.append("GROUP BY su.id ");
        if (searchMap.containsKey("order")) {
            sb.append("ORDER BY " + searchMap.get("order") + " DESC");
        }

        return sb.toString();
    }
}
