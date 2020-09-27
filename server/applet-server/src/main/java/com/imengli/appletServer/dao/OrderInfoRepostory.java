package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.OrderInfoDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:17
 * @description: 订单操作
 * @version: v1
 */
@Mapper
public interface OrderInfoRepostory {

    @Insert("INSERT INTO order_info (userId,createBy,createDate,applyBox,retreatBox,totalPrice) " +
            "VALUES (#{orderInfo.userId},#{orderInfo.createBy},#{orderInfo.createDate},#{orderInfo.applyBox},#{orderInfo.retreatBox},#{orderInfo.totalPrice})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void save(@Param("orderInfo") OrderInfoDO orderInfo);
}
