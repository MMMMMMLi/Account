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
    private Integer id;

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
