package com.imengli.orderserver.dao.provide;

import com.imengli.orderserver.daomain.OrderInfoDetailDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:30
 * @description: 自定义SQL插入
 * @version: v1
 */
public class MineInsertProvider {

    private final String ORDER_INFO_DETAIL_TABLE_NAME = "order_info_detail";

    public String insertOrderInfoDetail(@Param("orders") List<OrderInfoDetailDO> orders) {
        return new SQL() {{
            INSERT_INTO(ORDER_INFO_DETAIL_TABLE_NAME);
            INTO_COLUMNS("orderId", "categoryValue", "sizeValue", "gross", "tare","unitPrice","totalPrice");
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = orders.size(); i < len; i++) {
                if (i > 0) {
                    sb.append(") , (");
                }
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
}
