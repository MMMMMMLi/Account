package com.imengli.appletServer.dao;

import com.imengli.appletServer.daomain.OrderInfoDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:17
 * @description: 订单操作
 * @version: v1
 */
@Mapper
public interface OrderInfoRepostory {

    @Insert("INSERT INTO order_info (userId,createBy,createDate,applyBox,retreatBox,totalPrice,totalWeight) " +
            "VALUES (#{orderInfo.userId},#{orderInfo.createBy},#{orderInfo.createDate},#{orderInfo.applyBox},#{orderInfo.retreatBox},#{orderInfo.totalPrice},#{orderInfo.totalWeight})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void save(@Param("orderInfo") OrderInfoDO orderInfo);

    @Select("select * from order_info where userId = #{userId} AND createDate BETWEEN #{startDate} AND #{endDate}")
    List<OrderInfoDO> select(@Param("userId") String userId, @Param("startDate")LocalDateTime startDate,@Param("endDate")LocalDateTime endDate);

    @Select("select * from order_info where createDate BETWEEN #{startDate} AND #{endDate}")
    List<OrderInfoDO> selectAllOrderList(@Param("startDate")LocalDateTime startDate,@Param("endDate")LocalDateTime endDate);
}
