package com.imengli.appletServer.schedule;

import com.alibaba.fastjson.JSON;
import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.daomain.OrderInfoDO;
import com.imengli.appletServer.service.WechatAuthService;
import com.imengli.appletServer.utils.HttpUtil;
import com.imengli.appletServer.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/12/4 15:25
 * @description: 每天推送订单消息
 * @version:
 */
@Component
public class SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingConfigurer.class);

    @Value("${applet.appID}")
    private String appID;

    @Value("${applet.appSecret}")
    private String appSecret;

    @Value("${applet.accessTokenUri}")
    private String accessTokenUri;

    @Value("${applet.wechatTokenKey}")
    private String wechatTokenKey;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Autowired
    private WechatAuthService wechatAuthService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 每天的晚上18/19点01分01秒定时推送订单信息
     * 19点的主要是为了给18点发送失败填窟窿的。
     */
    @Scheduled(cron = "1 1 18,19 * * ?")
    public void subMessage() {
        LOG.info(">>>>>> 开始执行消息推送。");
        // 获取今天的所有订单
        List<OrderInfoDO> orderInfoDOS = orderInfoRepostory.selectAllOrderList(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), new ArrayList<>());
        LOG.info(">>>>>> 今天需要推送：{}条消息。", orderInfoDOS.parallelStream()
                // 过滤已经发送过的订单信息
                .filter(info -> !info.getIsNotice())
                // TODO 是否需要过滤掉收费的订单？
                // .filter(info -> info.getStatus() == 0)
                // 根据用户ID分组
                .collect(Collectors.groupingBy(info -> info.getUserId())).size());
        // 今天如果存在订单，则操作
        Integer size = 0;
        if (orderInfoDOS.size() > 0) {
            try {
                size = wechatAuthService.sendMsgToWechat(orderInfoDOS);
            } catch (Exception e) {
                LOG.info(">>>>>> 自动推送失败，{}", e.getMessage());
            }
        }
        LOG.info(">>>>>> 已经成功推送：{}条消息。", size);
        LOG.info(">>>>>> 推送完毕。");
    }


    /**
     * 定时更新微信小程序服务端的Token值,下面摘自微信的说明：
     * access_token 的存储至少要保留 512 个字符空间；
     * access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     * access_token 的有效期通过返回的 expires_in 来传达，目前是7200秒之内的值，中控服务器需要根据这个有效时间提前去刷新。
     * 在刷新过程中，中控服务器可对外继续输出的老 access_token，此时公众平台后台会保证在5分钟内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
     * access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，
     * 这样便于业务服务器在API调用获知 access_token 已超时的情况下，可以触发 access_token 的刷新流程。
     */
    // 微信的Token值有效时间为7200S，两个小时，每次提前10S更新有效值。
    @Scheduled(fixedDelay = 7190 * 1000)
    public void updateWechatAccessToken() throws InterruptedException {
        LOG.info(">>>>>> 开始更新微信Token。 ");
        String sccessToken = null;
        try {
            sccessToken = HttpUtil.execLink(String.format(this.accessTokenUri, this.appID, this.appSecret));
        } catch (IOException e) {
            LOG.error(">>>>>> 获取微信Token失败，一分钟之后重新执行。");
            TimeUnit.MINUTES.sleep(1L);
            this.updateWechatAccessToken();
        }
        if (StringUtils.isNotBlank(sccessToken)) {
            LOG.info(">>>>>> 获取微信Token成功，开始更新Redis。 ");
        }
        // 格式化返回信息
        Map<String, Object> retureInfo = JSON.parseObject(sccessToken, Map.class);
        // 判断是否包含操作信息
        if (retureInfo.containsKey("errcode")) {
            LOG.info(">>>>>> 更新微信Token失败，原因为：{}，一分钟之后重新执行。", retureInfo.get("errmsg"));
            TimeUnit.MINUTES.sleep(1L);
            this.updateWechatAccessToken();
        } else {
            // 存入Redis中。
            boolean set = redisUtil.set(this.wechatTokenKey,
                    String.valueOf(retureInfo.get("access_token")),
                    Long.valueOf(String.valueOf(retureInfo.get("expires_in"))) / 60 / 60);
            if (set && redisUtil.contains(this.wechatTokenKey)) {
                LOG.info(">>>>>> 更新微信Token成功。");
            } else {
                LOG.info(">>>>>> 更新微信Token失败。");
            }
        }

    }
}
