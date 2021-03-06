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
import com.imengli.appletServer.daomain.StockInfoDO;
import com.imengli.appletServer.daomain.StockInfoDeatilDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:14
 * @description: 库存管理
 * @version: v1
 */
@Mapper
public interface StockRepostory {

    @Insert("REPLACE INTO " + SysConstant.STOCK_INFO_TABLE_NAME +
            " (`key`,category,`size`,`number`,updateDate,updateBy) " +
            " Values (#{info.key},#{info.category},#{info.size},#{info.number},#{info.updateDate},#{info.updateBy})")
    void insertStock(@Param("info") StockInfoDO stockInfoDO);

    @Select("SELECT * FROM " + SysConstant.STOCK_INFO_TABLE_NAME +
            " Where `key` = #{key} and category = #{category}")
    StockInfoDO getStockInfoByKeyAndCategory(@Param("key") Integer stockKey, @Param("category") String category);

    @Select("SELECT * FROM " + SysConstant.STOCK_INFO_TABLE_NAME +
            " Where `key` = #{key} and number > 0 order by number desc")
    List<StockInfoDO> getStockInfoByKey(@Param("key") Integer stockKey);

    @Insert("INSERT INTO " + SysConstant.STOCK_INFO_DEATIL_TABLE_NAME +
            " (userId,orderId,`key`,category,`size`,`number`,`type`,operationDate,operationBy) " +
            " VALUES " +
            " (#{info.userId},#{info.orderId},#{info.key},#{info.category},#{info.size}," +
            "  #{info.number},#{info.type},#{info.operationDate},#{info.operationBy})")
    void addStockInfoDetail(@Param("info") StockInfoDeatilDO stockInfoDeatil);

    @Select("SELECT " +
            "   DATE_FORMAT(sid.operationDate,'%Y-%m-%d %H:%i:%s') AS operationDate," +
            "   su.userName,sid.category,sid.`key`,sid.type,sid.number,sid.`size` " +
            " FROM " + SysConstant.STOCK_INFO_DEATIL_TABLE_NAME + " sid " +
            " LEFT JOIN " + SysConstant.USER_TABLE_NAME + " su ON sid.userId = su.id " +
            " WHERE sid.`key` = #{key} and su.state = 1 ORDER BY sid.operationDate DESC")
    List<Map<String,Object>> getStockInfoDetailByKey(@Param("key") Integer stockKey);

    @Update("UPDATE " + SysConstant.STOCK_INFO_TABLE_NAME +
            " set `number` = #{info.number}, updateDate = #{info.updateDate}, updateBy = #{info.updateBy} where category = #{info.category} and `key` = 0")
    void updateStockInfoByItem(@Param("info") StockInfoDO info);
}
