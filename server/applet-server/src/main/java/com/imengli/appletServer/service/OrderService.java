package com.imengli.appletServer.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.OrderInfoDetailRepostory;
import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.dao.SysUserRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.*;
import com.imengli.appletServer.dto.AddOrderFormInfoPOJO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/27 14:00
 * @description: 订单相关Service
 * @version: v1
 */
@Service
public class OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private SysUserRepostory sysUserRepostory;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Resource
    private OrderInfoDetailRepostory orderInfoDetailRepostory;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String TYPE = "paidStatus";

    public WechatUserDO getWechatAuthEntity(String token) {
        WechatAuthDO wechatAuthDO = null;
        WechatUserDO wechatUserDOByOpenId = null;
        // 检验Token是否正常
        if (redisUtil.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthDO = redisUtil.getWechat(token);
        }
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
        }
        return wechatUserDOByOpenId;
    }

    /**
     * 保存订单信息
     *
     * @param orderFormInfo
     * @return 状态
     */
    @Transactional
    public ResultDTO insertOrderInfo(AddOrderFormInfoPOJO orderFormInfo) {
        // 校验token
        WechatUserDO wecharUserDo = this.getWechatAuthEntity(orderFormInfo.getToken());
        // 判断是否异常
        // 根据信息完善度返回
        if (wecharUserDo != null) {
            // 处理Form信息
            // 下订单的用户信息
            SysUserDO userInfo = orderFormInfo.getUserInfo();
            // 创建订单
            OrderInfoDO orderInfoDO = OrderInfoDO.builder()
                    .userId(userInfo.getId())
                    .createBy(wecharUserDo.getUserId())
                    .createDate(LocalDateTime.now())
                    .applyBox(orderFormInfo.getApplyBox())
                    .retreatBox(orderFormInfo.getRetreatBox())
                    .totalPrice(orderFormInfo.getTotalPrice())
                    .totalWeight(orderFormInfo.getOrders().parallelStream()
                            .map(order -> order.getGross() - order.getTare())
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
                        order.setOrderId(orderInfoDO.getId());
                        return order;
                    })
                    .collect(Collectors.toList());
            // 创建订单详情
            Integer size = orderInfoDetailRepostory.save(orders);
            // 判断是否插入正常
            if (orders.size() == size) {
                return new ResultDTO<>(ResultStatus.SUCCESS, orderInfoDO.getId());
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
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // 获取用户信息
            SysUserDO userInfoById = sysUserRepostory.getUserInfoById(wechatUserDO.getUserId());
            if (userInfoById != null) {
                PageHelper.startPage(page, size);
                // 查询用户订单
                List<OrderInfoDO> orderInfoDOS = orderInfoRepostory.select(userInfoById.getId(), LocalDateTime.of(LocalDate.now().minusDays(status), LocalTime.of(00, 00, 00)), LocalDateTime.now());
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
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            PageHelper.startPage(page, size);
            // 查询所有订单信息
            List<OrderInfoDO> orderInfoDOS =
                    orderInfoRepostory.selectAllOrderList(
                            LocalDateTime.of(LocalDate.now().minusDays(status), LocalTime.of(00, 00, 00)),
                            LocalDateTime.now(),
                            JSON.parseArray(filterList, HashMap.class));
            PageInfo<OrderInfoDO> orderInfoDOPageInfo = new PageInfo<>(orderInfoDOS);
            orderInfoDOPageInfo.setList(null);
            // 构建返回信息
            return new ResultDTO(ResultStatus.SUCCESS,
                    orderInfoDOS.parallelStream()
                            .map(orderInfoDO ->
                                    AddOrderFormInfoPOJO.builder()
                                            .userInfo(sysUserRepostory.getUserInfoById(orderInfoDO.getUserId()))
                                            .totalPrice(orderInfoDO.getTotalPrice())
                                            .totalWeight(orderInfoDO.getTotalWeight())
                                            .applyBox(orderInfoDO.getApplyBox())
                                            .retreatBox(orderInfoDO.getRetreatBox())
                                            .createDate(dateTimeFormatter.format(orderInfoDO.getCreateDate()))
                                            .orders(orderInfoDetailRepostory.select(orderInfoDO.getId()))
                                            .status(SysConstant.getValueByTypeAndKey(TYPE, String.valueOf(orderInfoDO.getStatus())).toString())
                                            .collectionTime(orderInfoDO.getCollectionTime() != null ? orderInfoDO.getCollectionTime().format(dateTimeFormatter) : "")
                                            .build()
                            )
                            .sorted(Comparator.comparing(AddOrderFormInfoPOJO::getCreateDate).reversed())
                            .collect(Collectors.toList())
                    , orderInfoDOPageInfo
            );
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
