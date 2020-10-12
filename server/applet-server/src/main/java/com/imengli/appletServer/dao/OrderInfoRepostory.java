package com.imengli.appletServer.dao;

import com.imengli.appletServer.dao.provide.MineSelectProvider;
import com.imengli.appletServer.daomain.OrderInfoDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 16:17
 * @description: 订单操作
 * @version: v1
 */
@Mapper
public interface OrderInfoRepostory {

    @Insert("INSERT INTO order_info (userId,createBy,createDate,updateDate,applyBox,retreatBox,totalPrice,totalWeight) " +
            "VALUES (#{orderInfo.userId},#{orderInfo.createBy},#{orderInfo.createDate},#{orderInfo.updateDate},#{orderInfo.applyBox},#{orderInfo.retreatBox},#{orderInfo.totalPrice},#{orderInfo.totalWeight})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(@Param("orderInfo") OrderInfoDO orderInfo);

    @Select("select * from order_info where userId = #{userId} AND createDate BETWEEN #{startDate} AND #{endDate}")
    List<OrderInfoDO> select(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @SelectProvider(value = MineSelectProvider.class, method = "selectOrderInfo")
    List<OrderInfoDO> selectAllOrderList(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("filterList") List<HashMap> filterList);

    @Update("update order_info set collectionTime = #{now},status = 1  where id = #{orderId}")
    void confirmCollection(@Param("orderId") Integer orderId, @Param("now") LocalDateTime now);

    @Select("select count(*) from order_info where id = #{orderId}")
    Integer getOrderInfoCountByOrderId(@Param("orderId") Integer orderInfoId);

    @Delete("delete " +
            "order_info,order_info_detail " +
            "from order_info " +
            "left join  order_info_detail on order_info.id =  order_info_detail.orderId " +
            "where order_info.id = #{orderId}")
    void deleteOrderInfoAndOrderInfoDeatils(@Param("orderId") Integer orderInfoId);
}
