/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.provide.MineSelectProvider;
import com.imengli.appletServer.daomain.OrderInfoDO;
import com.imengli.appletServer.daomain.OrderInfoDetailDO;
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

    @Insert("INSERT INTO " + SysConstant.ORDER_INFO_TABLE_NAME + " (id,userId,createBy,createDate,updateDate,applyBox,retreatBox,totalPrice,totalWeight) " +
            "VALUES (#{orderInfo.id},#{orderInfo.userId},#{orderInfo.createBy},#{orderInfo.createDate},#{orderInfo.updateDate},#{orderInfo.applyBox},#{orderInfo.retreatBox},#{orderInfo.totalPrice},#{orderInfo.totalWeight})")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(@Param("orderInfo") OrderInfoDO orderInfo);

    @Select("select * from " + SysConstant.ORDER_INFO_TABLE_NAME + " where userId = #{userId} AND createDate BETWEEN #{startDate} AND #{endDate}")
    List<OrderInfoDO> select(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @SelectProvider(value = MineSelectProvider.class, method = "selectOrderInfo")
    List<OrderInfoDO> selectAllOrderList(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("filterList") List<HashMap> filterList);

    @Update("update " + SysConstant.ORDER_INFO_TABLE_NAME + " set collectionTime = #{now},status = 1  where id = #{orderId}")
    void confirmCollection(@Param("orderId") Long orderId, @Param("now") LocalDateTime now);

    @Delete("delete " + SysConstant.ORDER_INFO_TABLE_NAME +
            "," + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME +
            " from " + SysConstant.ORDER_INFO_TABLE_NAME +
            " left join " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " on order_info.id =  order_info_detail.orderId " +
            "where order_info.id = #{orderId}")
    void deleteOrderInfoAndOrderInfoDeatils(@Param("orderId") Long orderInfoId);

    @Update("update " + SysConstant.ORDER_INFO_TABLE_NAME + " set isNotice = 1 where id in (${orderIds}) ")
    void updateOrderNoticeFlag(@Param("orderIds") String orderIds);

    @Select("SELECT * FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " WHERE DATE_FORMAT(createDate, '%Y-%m-%d') = CURDATE() AND isNotice = 0 AND userId = #{userId}")
    List<OrderInfoDO> getUserOrder(@Param("userId") String userId);

    @Select("SELECT * FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " WHERE `id` = #{orderId} ")
    List<OrderInfoDO> getOrderInfoByOrderId(@Param("orderId") String id);

    @Select("SELECT * From " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " where orderId in (${join}) ")
    List<OrderInfoDetailDO> getOrderInfoDetailsByOrderId(@Param("join") String join);
}
