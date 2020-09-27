package com.imengli.orderserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imengli.orderserver.daomain.OrderInfoDetailDO;
import com.imengli.orderserver.daomain.SysUserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 11:17
 * @description: 订单页面提交的Form表单内容
 * @version: v1
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderFormInfoPOJO implements Serializable {

    /**
     * form表单的标志,没用。
     */
    private String orderListId;

    /**
     * 当前操作的用户Token
     */
    @JsonProperty(value = "token")
    private String token;

    /**
     * 用框数量
     */
    @JsonProperty(value = "applyBox")
    private Integer applyBox;

    /**
     * 退框数量
     */
    @JsonProperty(value = "retreatBox")
    private Integer retreatBox;

    /**
     * 当前订单金额
     */
    @JsonProperty(value = "totalPrice")
    private Double totalPrice;

    /**
     * 用户信息
     */
    @JsonProperty(value = "userInfo")
    private SysUserDO userInfo;

    /**
     * 订单信息
     */
    @JsonProperty(value = "orders")
    private List<OrderInfoDetailDO> orders;


}
