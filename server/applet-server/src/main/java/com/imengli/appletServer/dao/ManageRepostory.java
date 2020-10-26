package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/26 10:02
 * @description: 曾有樱花落海洋
 * @version: v1
 */
@Mapper
public interface ManageRepostory {

    @Select("SELECT " +
            "COUNT(1) AS orders," +
            "SUM(totalPrice) AS totalPrice, " +
            "SUM(totalWeight) AS totalWeight " +
            "FROM  " + SysConstant.ORDER_INFO_TABLE_NAME +
            " WHERE createDate BETWEEN CONCAT(CURDATE(),' 00:00:00')  AND CONCAT(CURDATE(),' 23:59:59')")
    Map<String, Double> getSummaryOrderInfo();

    @Select(" SELECT " +
            " categoryValue as category, " +
            " SUM(gross - tare) as sumWeight, " +
            " DATE_FORMAT(oi.createDate,'%m-%d') as dateTime " +
            " FROM " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " oid " +
            " LEFT JOIN " + SysConstant.ORDER_INFO_TABLE_NAME + " oi ON oid.orderId = oi.id " +
            " WHERE oi.createDate BETWEEN #{startTime} AND #{endTime} " +
            " GROUP BY categoryValue,DATE_FORMAT(oi.createDate,'%Y-%m-%d') " +
            " ORDER BY DATE_FORMAT(oi.createDate, '%Y-%m-%d'),sumWeight DESC")
    List<Map<String, Object>> getReportByCategory(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select(" SELECT " +
            " sizeValue as size, " +
            " SUM(gross - tare) as sumWeight, " +
            " DATE_FORMAT(oi.createDate,'%m-%d') as dateTime " +
            " FROM " + SysConstant.ORDER_INFO_DEATIL_TABLE_NAME + " oid " +
            " LEFT JOIN " + SysConstant.ORDER_INFO_TABLE_NAME + " oi ON oid.orderId = oi.id " +
            " WHERE oi.createDate BETWEEN #{startTime} AND #{endTime} " +
            " GROUP BY sizeValue,DATE_FORMAT(oi.createDate,'%Y-%m-%d') " +
            " ORDER BY DATE_FORMAT(oi.createDate, '%Y-%m-%d'),sumWeight DESC")
    List<Map<String, Object>> getReportBySize(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select(" SELECT " +
            " su.userName, " +
            " SUM(oi.totalPrice) AS totalPrice " +
            " FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " oi " +
            " LEFT JOIN " + SysConstant.USER_TABLE_NAME + " su ON oi.userId = su.id " +
            " WHERE oi.createDate BETWEEN #{startTime} AND #{endTime} " +
            " GROUP BY su.userName " +
            " ORDER BY totalPrice DESC " +
            " LIMIT 10 ")
    List<Map<String, Object>> getReportByPerson(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
