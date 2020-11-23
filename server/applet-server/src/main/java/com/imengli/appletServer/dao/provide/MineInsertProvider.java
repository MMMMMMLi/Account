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
            INTO_COLUMNS("orderId", "categoryValue", "sizeValue", "gross", "tare", "unitPrice", "totalPrice");
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
