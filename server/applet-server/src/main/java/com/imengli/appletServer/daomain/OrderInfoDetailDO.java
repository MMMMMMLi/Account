package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDetailDO {

    /**
     * 订单详情主键
     */
    private Integer id;

    /**
     * 订单的Id
     */
    private Integer orderId;

    /**
     * 品种
     */
    private Integer categoryValue;

    /**
     * 大小
     */
    private Integer sizeValue;

    /**
     * 毛重
     */
    private Double gross;

    /**
     * 皮重
     */
    private Double tare;

    /**
     * 单价
     */
    private Double unitPrice;

    /**
     * 总价
     */
    private Double totalPrice;

}
