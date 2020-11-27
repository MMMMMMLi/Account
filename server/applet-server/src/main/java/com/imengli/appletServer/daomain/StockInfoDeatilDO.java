package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/26 11:42
 * @description: 库存变更详情表
 * @version: v1
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInfoDeatilDO {

    private Integer id;

    private String userId;

    private String orderId;

    private Integer key;

    private String category;

    private String size;

    private Double number;

    private Integer type;

    private LocalDateTime operationDate;

    private String operationBy;
}
