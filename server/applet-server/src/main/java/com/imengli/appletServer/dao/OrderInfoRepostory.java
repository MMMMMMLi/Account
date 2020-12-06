package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
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

    @Insert("INSERT INTO " + SysConstant.ORDER_INFO_TABLE_NAME + " (userId,createBy,createDate,updateDate,applyBox,retreatBox,totalPrice,totalWeight) " +
            "VALUES (#{orderInfo.userId},#{orderInfo.createBy},#{orderInfo.createDate},#{orderInfo.updateDate},#{orderInfo.applyBox},#{orderInfo.retreatBox},#{orderInfo.totalPrice},#{orderInfo.totalWeight})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(@Param("orderInfo") OrderInfoDO orderInfo);

    @Select("select * from " + SysConstant.ORDER_INFO_TABLE_NAME + " where userId = #{userId} AND createDate BETWEEN #{startDate} AND #{endDate}")
    List<OrderInfoDO> select(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @SelectProvider(value = MineSelectProvider.class, method = "selectOrderInfo")
    List<OrderInfoDO> selectAllOrderList(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("filterList") List<HashMap> filterList);

    @Update("update " + SysConstant.ORDER_INFO_TABLE_NAME + " set collectionTime = #{now},status = 1  where id = #{orderId}")
    void confirmCollection(@Param("orderId") Integer orderId, @Param("now") LocalDateTime now);

    @Select("select count(*) from " + SysConstant.ORDER_INFO_TABLE_NAME + " where id = #{orderId}")
    Integer getOrderInfoCountByOrderId(@Param("orderId") Integer orderInfoId);

    @Delete("delete " + SysConstant.ORDER_INFO_TABLE_NAME +
            "," + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME +
            " from " + SysConstant.ORDER_INFO_TABLE_NAME +
            " left join " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " on order_info.id =  order_info_detail.orderId " +
            "where order_info.id = #{orderId}")
    void deleteOrderInfoAndOrderInfoDeatils(@Param("orderId") Integer orderInfoId);

    @Select("SELECT GROUP_CONCAT(DISTINCT oid.categoryValue) " +
            "FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " oi " +
            "LEFT JOIN " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " oid ON oi.id = oid.orderId " +
            "WHERE oi.createDate BETWEEN #{startDate} AND #{endDate} AND oi.userId = #{userId} " +
            "GROUP BY userId;")
    String getOrderCategoryByUserId(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Update("update " + SysConstant.ORDER_INFO_TABLE_NAME + " set isNotice = 1 where id in (${orderIds}) ")
    void updateOrderNoticeFlag(@Param("orderIds") String orderIds);

    @Select("SELECT * FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " WHERE DATE_FORMAT(createDate, '%Y-%m-%d') = CURDATE() AND isNotice = 0 AND userId = #{userId}")
    List<OrderInfoDO> getUserOrder(@Param("userId") String userId);

    @Select("SELECT * FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " WHERE `id` = #{orderId} ")
    List<OrderInfoDO> getOrderInfoByOrderId(@Param("orderId") String id);
}
