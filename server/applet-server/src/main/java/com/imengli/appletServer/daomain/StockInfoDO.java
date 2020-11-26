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
