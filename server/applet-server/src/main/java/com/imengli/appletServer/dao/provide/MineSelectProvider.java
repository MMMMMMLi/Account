package com.imengli.appletServer.dao.provide;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/10 11:30
 * @description: 自定义SQL查询
 * @version: v1
 */
public class MineSelectProvider {

    private final String SYS_USER_TABLE_NAME = "sys_user";
    private final String ORDER_INFO_TABLE_NAME = "order_info";
    private final String ORDER_INFO_DETAIL_TABLE_NAME = "order_info_detail";

    public String selectOrderInfo(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  @Param("filterList") List<HashMap> filterList) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT " +
                "`order`.* " +
                "FROM " +
                "order_info `order` " +
                "LEFT JOIN order_info_detail detail ON `order`.id = detail.orderId " +
                "LEFT JOIN sys_user `user` ON `order`.userId = `user`.id " +
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
}
