package com.imengli.orderserver.dao;

import com.imengli.orderserver.dao.provide.MineInsertProvider;
import com.imengli.orderserver.daomain.OrderInfoDetailDO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:17
 * @description: 订单详情操作
 * @version: v1
 */
@Mapper
public interface OrderInfoDetailRepostory {

    @InsertProvider(value = MineInsertProvider.class, method = "insertOrderInfoDetail")
    Integer save(@Param("orders") List<OrderInfoDetailDO> orders);
}
