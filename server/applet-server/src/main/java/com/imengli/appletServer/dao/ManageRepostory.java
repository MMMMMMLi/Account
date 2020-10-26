package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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

    Map<String, Object> getReportByCategory(LocalDateTime startTime, LocalDateTime endTime);
}
