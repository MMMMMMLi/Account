package com.imengli.orderserver.service;

import com.imengli.orderserver.common.ResultStatus;
import com.imengli.orderserver.dao.OrderInfoDetailRepostory;
import com.imengli.orderserver.dao.OrderInfoRepostory;
import com.imengli.orderserver.dao.WechatUserRepostory;
import com.imengli.orderserver.daomain.*;
import com.imengli.orderserver.dto.AddOrderFormInfoPOJO;
import com.imengli.orderserver.dto.ResultDTO;
import com.imengli.orderserver.remote.RedisWechatRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
    private RedisWechatRemote redisWechatRemote;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Resource
    private OrderInfoDetailRepostory orderInfoDetailRepostory;

    public WechatAuthDO getWechatAuthEntity(String token) {
        WechatAuthDO wechatAuthDO = null;
        // 检验Token是否正常
        if (redisWechatRemote.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthDO = redisWechatRemote.getWechat(token);
        }
        return wechatAuthDO;
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
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(orderFormInfo.getToken());
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            WechatUserDO wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
            // 根据信息完善度返回
            if (wechatUserDOByOpenId != null) {
                // 处理Form信息
                // 下订单的用户信息
                SysUserDO userInfo = orderFormInfo.getUserInfo();
                // 创建订单
                OrderInfoDO orderInfoDO = OrderInfoDO.builder()
                        .userId(userInfo.getId())
                        .createBy(wechatUserDOByOpenId.getUserId())
                        .createDate(new Date())
                        .applyBox(orderFormInfo.getApplyBox())
                        .retreatBox(orderFormInfo.getRetreatBox())
                        .totalPrice(orderFormInfo.getTotalPrice())
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
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
