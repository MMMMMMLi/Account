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
}
