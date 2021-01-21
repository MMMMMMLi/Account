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
import com.imengli.appletServer.dao.provide.MineInsertProvider;
import com.imengli.appletServer.dao.provide.MineSelectProvider;
import com.imengli.appletServer.daomain.SysUserDO;
import org.apache.ibatis.annotations.*;

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
            "SUM(totalWeight) AS totalWeight, " +
            "(SELECT SUM(number) from " + SysConstant.STOCK_INFO_TABLE_NAME + " where `key` = 0 ) AS totalStock " +
            " FROM " + SysConstant.ORDER_INFO_TABLE_NAME +
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
            " WHERE oi.createDate BETWEEN #{startTime} AND #{endTime} and su.state = 1 " +
            " GROUP BY su.userName " +
            " ORDER BY totalPrice DESC " +
            " LIMIT 10 ")
    List<Map<String, Object>> getReportByPerson(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select(" SELECT " +
            " SUM(oi.totalPrice) AS sumPrice, " +
            " SUM(oi.totalWeight) AS sumWeight, " +
            " DATE_FORMAT(oi.createDate, '%H') AS dateTime " +
            " FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " oi " +
            " WHERE oi.createDate BETWEEN #{startTime} AND #{endTime} " +
            " GROUP BY DATE_FORMAT(oi.createDate, '%Y-%m-%d %H') " +
            " ORDER BY dateTime ")
    List<Map<String, Object>> getReportByTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @SelectProvider(value = MineSelectProvider.class, method = "getUserList")
    List<Map<String, Object>> getUserList(Map<String, String> searchMap);

    @Select(" SELECT " +
            " 	su.userName, " +
            " 	su.phoneNumber, " +
            "   su.address, " +
            "   CASE su.gender WHEN '1' THEN '男' WHEN '2' THEN '女' END AS gender, " +
            "   DATE_FORMAT(su.updateTime, '%Y-%m-%d %H:%i:%s') AS updateTime," +
            " 	DATE_FORMAT(oi.createDate, '%Y-%m-%d') AS createDate, " +
            " 	DATE_FORMAT(oi.createDate, '%H:%i:%s') AS createTime, " +
            " 	oi.applyBox, " +
            " 	oi.retreatBox, " +
            " 	oi.totalPrice, " +
            " 	oi.totalWeight, " +
            " 	oi.`status`, " +
            " 	su.isTemp " +
            " FROM " + SysConstant.USER_TABLE_NAME + " su " +
            " LEFT JOIN " + SysConstant.ORDER_INFO_TABLE_NAME + " oi ON su.id = oi.userId " +
            " WHERE " +
            " 	su.id = #{userId} and su.state = 1 " +
            " ORDER BY oi.createDate DESC")
    List<Map<String, Object>> getUserDetails(@Param("userId") String userId);

    @Select("SELECT id,`key`,`value`,`status` FROM " + SysConstant.CONSTANT_TABLE_NAME + " WHERE type = 'web' ORDER BY `key`, id;")
    List<Map<String, Object>> getSysContant();

    @InsertProvider(value = MineInsertProvider.class, method = "insertSystemInfo")
    void insertSystemInfo(List<Map<String, Object>> insertInfos);

    @Update(" update " + SysConstant.CONSTANT_TABLE_NAME +
            " set `value` = #{info.value}, updateDate = now(), `status` = #{info.status}" +
            " where id = #{info.id}")
    void updateSystemInfo(@Param("info") Map<String, Object> info);

    @Select("SELECT " +
            " COUNT(1) AS countDay," +
            " SUM(oi.totalPrice) AS totalPrice," +
            " su.*" +
            "FROM " + SysConstant.ORDER_INFO_TABLE_NAME + " oi " +
            "LEFT JOIN " + SysConstant.USER_TABLE_NAME + " su ON oi.userId = su.id " +
            "WHERE oi.status = 0  and su.state = 1 " +
            "and oi.collectionTime is null " +
            "and TIMESTAMPDIFF(DAY,DATE(oi.createDate),CURDATE()) <= #{days} " +
            "group by oi.userId " +
            "ORDER BY totalPrice DESC ")
    List<Map<String, Object>> getWarnList(@Param("days") int days);

    @Select(" SELECT " +
            "  * " +
            " FROM " + SysConstant.USER_TABLE_NAME +
            " WHERE " +
            "   ( LOCATE(#{userName}, userName) " +
            "   OR phoneNumber = #{phoneNumber} ) " +
            " AND isTemp = 0 and state = 1 " +
            " AND id != #{userId}")
    List<SysUserDO> getMergeUserInfo(@Param("userId") String userId, @Param("userName") String userName, @Param("phoneNumber") String phoneNumber);

    @Update(" UPDATE " + SysConstant.ORDER_INFO_TABLE_NAME +
            " oi LEFT JOIN " + SysConstant.STOCK_INFO_DEATIL_TABLE_NAME + " sid ON oi.userId = sid.userId " +
            " LEFT JOIN "+SysConstant.USER_TABLE_NAME+" su ON su.id = oi.userId "+
            " SET oi.userId = #{mergeUserId}, sid.userId = #{mergeUserId}, su.state = 0 " +
            " WHERE oi.userId = #{userId}")
    void mergeInfoByUserId(@Param("userId") String userId, @Param("mergeUserId") String mergeUserId);
}
