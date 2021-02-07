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
import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.OrderInfoDetailRepostory;
import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.dao.SysUserRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.OrderInfoDO;
import com.imengli.appletServer.daomain.OrderInfoDetailDO;
import com.imengli.appletServer.daomain.SysUserDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.AddOrderFormInfoPOJO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import com.imengli.appletServer.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 14:00
 * @description: 订单相关Service
 * @version: v1
 */
@Service
public class OrderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private SysUserRepostory sysUserRepostory;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Autowired
    private StockService stockService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @Resource
    private OrderInfoDetailRepostory orderInfoDetailRepostory;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String TYPE = "paidStatus";

    /**
     * 保存订单信息
     *
     * @param orderFormInfo
     * @return 状态
     */
    @Transactional
    public ResultDTO insertOrderInfo(AddOrderFormInfoPOJO orderFormInfo) {
        // 校验token
        WechatUserDO wecharUserDo = redisUtil.getWechatAuthEntity(orderFormInfo.getToken());
        // 判断是否异常
        // 根据信息完善度返回
        if (wecharUserDo != null) {
            // 先删除存在的订单信息
            orderInfoRepostory.deleteOrderInfoAndOrderInfoDeatils(orderFormInfo.getOrderInfoId());

            // 处理Form信息
            // 下订单的用户信息
            SysUserDO userInfo = orderFormInfo.getUserInfo();
            // 创建订单
            OrderInfoDO orderInfoDO = OrderInfoDO.builder()
                    .id(snowflakeIdWorker.nextStringId())
                    .userId(userInfo.getId())
                    .createBy(wecharUserDo.getUserId())
                    .createDate(orderFormInfo.getCreateDate() == null ?
                            LocalDateTime.now() : LocalDateTime.parse(orderFormInfo.getCreateDate(), dateTimeFormatter))
                    .updateDate(LocalDateTime.now())
                    .applyBox(orderFormInfo.getApplyBox())
                    .retreatBox(orderFormInfo.getRetreatBox())
                    .totalPrice(orderFormInfo.getTotalPrice())
                    .totalWeight(orderFormInfo.getOrders().parallelStream()
                            .map(order -> order.getSuttle())
                            .reduce((k, v) -> k + v).get())
                    .build();
            // 入库
            orderInfoRepostory.save(orderInfoDO);
            // 订单详情信息,去除空订单信息和完善订单ID。
            List<OrderInfoDetailDO> orders = orderFormInfo.getOrders()
                    .parallelStream()
                    .filter(order -> order.getGross() != null
                            && order.getTare() != null
                            && order.getUnitPrice() != null)
                    .map(order -> {
                        order.setId(snowflakeIdWorker.nextStringId());
                        order.setOrderId(orderInfoDO.getId());
                        return order;
                    })
                    .collect(Collectors.toList());
            // 创建订单详情
            Integer size = orderInfoDetailRepostory.save(orders);

            // 插入库存详情信息
            orderFormInfo.getOrders()
                    .parallelStream()
                    .forEach(info -> {
                        // 修改库存信息
                        stockService.replaceStockInfo(0, info.getCategoryValue(),
                                info.getSizeValue(), StockTypeEnum.REDUCE, info.getSuttle(),
                                wecharUserDo.getUserId(), userInfo.getId(), String.valueOf(orderInfoDO.getId()));
                    });
            // 修改框子信息
            stockService.replaceStockInfo(1, "", null, StockTypeEnum.REDUCE,
                    Double.valueOf(orderFormInfo.getApplyBox() - orderFormInfo.getRetreatBox()),
                    wecharUserDo.getUserId(), userInfo.getId(), String.valueOf(orderInfoDO.getId()));

            // 判断是否插入正常
            if (orders.size() == size) {
                return new ResultDTO<>(ResultStatus.SUCCESS, orderInfoDO.getId());
            } else {
                // 如果不正常则回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 获取每个人的订单信息
     *
     * @param token
     * @param status
     * @return
     */
    public ResultDTO getMyOrderList(String token, Integer status, Integer page, Integer size) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // 获取用户信息
            SysUserDO userInfoById = sysUserRepostory.getUserInfoByUserId(wechatUserDO.getUserId());
            if (userInfoById != null) {
                PageHelper.startPage(page, size);
                // 查询用户订单
                List<OrderInfoDO> orderInfoDOS = orderInfoRepostory.select(userInfoById.getId(), LocalDateTime.of(LocalDate.now().minusDays(status), LocalTime.MIN), LocalDateTime.now());
                PageInfo<OrderInfoDO> orderInfoDOPageInfo = new PageInfo<>(orderInfoDOS);
                orderInfoDOPageInfo.setList(null);
                // 构建返回信息
                return new ResultDTO(ResultStatus.SUCCESS,
                        orderInfoDOS.parallelStream()
                                .map(orderInfoDO ->
                                        AddOrderFormInfoPOJO.builder()
                                                .totalPrice(orderInfoDO.getTotalPrice())
                                                .totalWeight(orderInfoDO.getTotalWeight())
                                                .applyBox(orderInfoDO.getApplyBox())
                                                .retreatBox(orderInfoDO.getRetreatBox())
                                                .createDate(dateTimeFormatter.format(orderInfoDO.getCreateDate()))
                                                .orders(orderInfoDetailRepostory.select(orderInfoDO.getId()))
                                                .status(SysConstant.getValueByTypeAndKey(TYPE, String.valueOf(orderInfoDO.getStatus())).toString())
                                                .build()
                                )
                                .sorted(Comparator.comparing(AddOrderFormInfoPOJO::getCreateDate).reversed())
                                .collect(Collectors.toList())
                        , orderInfoDOPageInfo
                );
            }
            return new ResultDTO(ResultStatus.SUCCESS, null);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 获取所有人的订单信息
     *
     * @param token
     * @param status
     * @param page
     * @param size
     * @param filterList
     * @return
     */
    public ResultDTO getOrderList(String token, Integer status, Integer page, Integer size, String filterList) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            PageHelper.startPage(page, size);
            // 查询所有订单信息
            List<OrderInfoDO> orderInfoDOS =
                    orderInfoRepostory.selectAllOrderList(
                            LocalDateTime.of(LocalDate.now().minusDays(status), LocalTime.MIN),
                            LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                            JSON.parseArray(filterList, HashMap.class));
            PageInfo<OrderInfoDO> orderInfoDOPageInfo = new PageInfo<>(orderInfoDOS);
            orderInfoDOPageInfo.setList(null);
            // 构建返回信息
            return new ResultDTO(ResultStatus.SUCCESS,
                    orderInfoDOS.parallelStream()
                            .map(orderInfoDO ->
                                    AddOrderFormInfoPOJO.builder()
                                            .orderInfoId(orderInfoDO.getId())
                                            .orderListId(orderInfoDO.getId())
                                            .userInfo(sysUserRepostory.getUserInfoByUserId(orderInfoDO.getUserId()))
                                            .totalPrice(orderInfoDO.getTotalPrice())
                                            .totalWeight(orderInfoDO.getTotalWeight())
                                            .applyBox(orderInfoDO.getApplyBox())
                                            .retreatBox(orderInfoDO.getRetreatBox())
                                            .createDate(dateTimeFormatter.format(orderInfoDO.getCreateDate()))
                                            .orders(orderInfoDetailRepostory.select(orderInfoDO.getId()))
                                            .status(SysConstant.getValueByTypeAndKey(TYPE, String.valueOf(orderInfoDO.getStatus())).toString())
                                            .collectionTime(orderInfoDO.getCollectionTime() != null ? orderInfoDO.getCollectionTime().format(dateTimeFormatter) : "")
                                            .isNotice(orderInfoDO.getIsNotice())
                                            .build()
                            )
                            .sorted(Comparator.comparing(AddOrderFormInfoPOJO::getCreateDate).reversed())
                            .collect(Collectors.toList())
                    , orderInfoDOPageInfo
            );
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 确认收款
     *
     * @param token
     * @param orderId
     * @return
     */
    public ResultDTO confirmCollection(String token, Long orderId) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            orderInfoRepostory.confirmCollection(orderId, LocalDateTime.now());
            return new ResultDTO(ResultStatus.SUCCESS_COLLECTION);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public ResultDTO getUserOrderSize(String userId) {
        List<OrderInfoDO> userOrderSize = orderInfoRepostory.getUserOrder(userId);
        return new ResultDTO(ResultStatus.SUCCESS, userOrderSize.size());
    }

    /**
     * 推送消息
     *
     * @param flag 标识为
     * @param id   为true传入的是userId，为false传入的是orderId
     * @return
     */
    public ResultDTO sendMsg(Boolean flag, String id) {
        List<OrderInfoDO> userOrders = new ArrayList<>();
        Optional<WechatUserDO> userEntityByUserId;
        // 一起发送，
        if (flag) {
            // 查看当前用户是否有openid
            userEntityByUserId = Optional.ofNullable(wechatUserRepostory.getUserEntityByUserId(id));

            userOrders = orderInfoRepostory.getUserOrder(id);
        } else {
            userOrders = orderInfoRepostory.getOrderInfoByOrderId(id);
            // 查看当前用户是否有openid
            userEntityByUserId = Optional.ofNullable(wechatUserRepostory.getUserEntityByUserId(userOrders.get(0).getUserId()));
        }
        // 校验用户是否为手动添加用户
        Boolean errorFlag = false;
        if (userEntityByUserId.isPresent()) {
            if (StringUtils.isBlank(userEntityByUserId.get().getOpenId())) {
                errorFlag = true;
            }
        } else {
            errorFlag = true;
        }
        if (errorFlag) {
            orderInfoRepostory.updateOrderNoticeFlag(
                    StringUtils.join(
                            userOrders.parallelStream()
                                    // 过滤掉发送失败的用户订单信息
                                    .map(info -> info.getId())
                                    .collect(Collectors.toList())
                            , ",")
            );
            return new ResultDTO(ResultStatus.ERROR.code(), "发送失败，当前用户为手动添加用户！");
        }
        try {
            wechatAuthService.sendMsgToWechat(userOrders);
        } catch (Exception e) {
            LOGGER.error(">>>>>> 消息推送失败，Msg：{}", e.getMessage());
            return new ResultDTO(ResultStatus.ERROR.code(), "消息推送失败！");
        }
        return new ResultDTO(ResultStatus.SUCCESS.code(), "消息推送成功！");
    }
}
