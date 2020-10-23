package com.imengli.appletServer.service;

import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/22 15:26
 * @description: 管理相关Service
 * @version: v1
 */
@Service
public class ManageService {

    private final static Logger log = LoggerFactory.getLogger(ManageService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

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
     * 获取当日的订单汇总信息
     *
     * @param token
     * @return
     */
    public ResultDTO getSummaryOrderInfo(String token) {
        // 校验token
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // TODO: 后续添加管理员校验

            return new ResultDTO(ResultStatus.SUCCESS, orderInfoRepostory.getSummaryOrderInfo());
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 根据不同的Type和State 获取报告
     *
     * @param token
     * @param type  category | size | person | time
     * @param state 0 | 7 | 30
     * @return
     */
    public ResultDTO getReport(String token, String type, Integer state) {
        // 校验token
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // TODO: 后续添加管理员校验

            // 设置起始时间
            LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(state), LocalTime.of(00, 00, 00));
            LocalDateTime endTime = LocalDateTime.now();

//            SELECT categoryValue,SUM(gross - tare),DATE_FORMAT(oi.createDate,'%Y-%m-%d') FROM order_info_detail oid
//            LEFT JOIN order_info oi ON oid.orderId = oi.id
//            WHERE oi.createDate BETWEEN '2020-10-21 00:00:00' AND '2020-10-22 23:59:59'
//            GROUP BY categoryValue,DATE_FORMAT(oi.createDate,'%Y-%m-%d')
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(00, 00, 00));
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println(startTime);

        System.out.println(endTime);
    }
}
