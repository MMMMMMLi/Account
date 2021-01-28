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

package com.imengli.appletServer.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.common.StockTypeEnum;
import com.imengli.appletServer.dao.StockRepostory;
import com.imengli.appletServer.daomain.StockInfoDO;
import com.imengli.appletServer.daomain.StockInfoDeatilDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:13
 * @description: 库存管理
 * @version: v1
 */
@Service
public class StockService {

    private static final Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Resource
    private StockRepostory stockRepostory;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加库存操作详情
     *
     * @param stockInfoDeatil
     */
    public void addStockInfoDetail(StockInfoDeatilDO stockInfoDeatil) {
        stockRepostory.addStockInfoDetail(stockInfoDeatil);
    }

    /**
     * 修改/添加库存信息
     *
     * @param stockKey        库存标识： 0 货物/ 1框子
     * @param category        品种
     * @param size            大小
     * @param type            操作类型
     * @param number          操作的数值
     * @param updateUserId    更新用户
     * @param operationUserId 操作用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void replaceStockInfo(Integer stockKey, String category, String size, StockTypeEnum type, Double number, String updateUserId, String operationUserId,String orderId) {
        // 查询当前库存相关库存的数量
        StockInfoDO stockInfoDO = stockRepostory.getStockInfoByKeyAndCategory(stockKey, category);
        // 如果已经存在,则修改库存量
        Double stockNumber = number;
        if (stockInfoDO != null) {
            if(type == StockTypeEnum.ADD) {
                stockNumber += stockInfoDO.getNumber();
            }
            if(type == StockTypeEnum.REDUCE) {
                stockNumber = stockInfoDO.getNumber() - stockNumber;
            }
        }
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 保存入库
        stockRepostory.insertStock(StockInfoDO.builder()
                .key(stockKey)
                .category(category)
                // TODO 总库存现在不区分大小个。
                // .size(size)
                .number(stockNumber)
                .updateDate(now)
                .updateBy(updateUserId)
                .build());
        // 保存操作日志入库
        this.addStockInfoDetail(StockInfoDeatilDO.builder()
                .userId(operationUserId)
                .orderId(orderId)
                .key(stockKey)
                .category(category)
                .size(size)
                .number(number)
                .type(type.key())
                .operationDate(now)
                .operationBy(updateUserId)
                .build());
    }

    /**
     * 添加库存
     *
     * @param stockKey 0: 货物 / 1: 框子
     * @param category
     * @param number
     * @return
     */
    @Transactional
    public ResultDTO insertStock(Integer stockKey, String category, Double number, String token) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // 如果添加库存的标识为框子的话,则品种置为空
            if (stockKey == 1) {
                category = "";
            }
            replaceStockInfo(stockKey, category, "", StockTypeEnum.ADD, number, wechatUserDO.getUserId(), wechatUserDO.getUserId(),null);
            return new ResultDTO(ResultStatus.SUCCESS);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 获取库存信息
     *
     * @param stockKey
     * @param token
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultDTO getStockInfo(Integer stockKey, String token, Integer pageNum, Integer pageSize) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            Map<String, Object> result = new HashMap<>();
            // 获取相关库存信息
            List<StockInfoDO> stockInfoDOList = stockRepostory.getStockInfoByKey(stockKey);
            // 获取操作详情
            PageHelper.startPage(pageNum, pageSize);
            List<Map<String, Object>> stockInfoDeatilDOList = stockRepostory.getStockInfoDetailByKey(stockKey);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(stockInfoDeatilDOList);

            // 库存信息
            result.put("stockInfo", stockInfoDOList);
            // 汇总信息
            result.put("stockSum", stockInfoDOList.parallelStream().map(info -> info.getNumber()).reduce((o1, o2) -> o1 + o2).get());
            // 操作详情
            result.put("stockInfoDeatil", pageInfo.getList());

            return new ResultDTO(ResultStatus.SUCCESS, result);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 校准库存信息
     *
     * @param jsonList
     * @param token
     * @return
     */
    @Transactional
    public ResultDTO alignStockInfo(String jsonList, String token) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // 格式化信息
            ArrayList<Map<String, String>> alignStockInfo = JSON.parseObject(jsonList, ArrayList.class);
            // 如果信息为空,则返回空
            if (alignStockInfo.size() > 0) {
                // 如果不是空,则迭代循环更新
                alignStockInfo.parallelStream().forEach(info -> {
                    // 获取当前时间
                    LocalDateTime now = LocalDateTime.now();
                    // 获取当前操作用户
                    String userId = wechatUserDO.getUserId();
                    // 更新库存信息
                    stockRepostory.updateStockInfoByItem(StockInfoDO.builder()
                            .category(info.get("category"))
                            .number(Double.valueOf(info.get("number")))
                            .updateDate(now)
                            .updateBy(userId)
                            .build());
                    // 更新库存详情信息
                    this.addStockInfoDetail(StockInfoDeatilDO
                            .builder()
                            .userId(userId)
                            .key(0)
                            .category(info.get("category"))
                            .number(Double.valueOf(info.get("number")))
                            .type(StockTypeEnum.ALIGN.key())
                            .operationDate(now)
                            .operationBy(userId)
                            .build());
                });
                // 不知道怎么校验,就直接返回成功就行
                return new ResultDTO(ResultStatus.SUCCESS);
            }
            return new ResultDTO(ResultStatus.NULL);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
