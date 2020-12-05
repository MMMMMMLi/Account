package com.imengli.appletServer.schedule;

import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.daomain.OrderInfoDO;
import com.imengli.appletServer.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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

    @Value("${applet.templateId}")
    private String templateId;

    @Value("${applet.page}")
    private String page;

    @Value("${applet.sendMsgUri}")
    private String sendMsgUri;

    @Value("${applet.wechatTokenKey}")
    private String wechatTokenKey;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 每天定时推送订单信息
     */
//    @Scheduled(cron = "* * * * * ?")
    public void subMessage() {
        LOG.info(">>>>>> 开始执行消息推送。");
        // 获取今天早晚时间.
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        // 获取今天的所有订单
        List<OrderInfoDO> orderInfoDOS = orderInfoRepostory.selectAllOrderList(
                startDateTime, endDateTime, new ArrayList<>());
        // 今天如果存在订单，则操作
        if (orderInfoDOS.size() > 0) {
            // 获取系统在微信端的Token
            String nowWechatToken = redisUtil.get(wechatTokenKey);
            // 构建推送请求的url
            String.format(sendMsgUri,nowWechatToken);
            // 迭代订单列表进行发送
            orderInfoDOS.parallelStream()
                    // 过滤已经发送过的订单信息
                    .filter(info -> !info.getIsNotice())
                    // 根据用户ID分组
                    .collect(Collectors.groupingBy(info -> info.getUserId()))
                    // 迭代通知
                    .forEach((userId, orderList) -> {
                        LOG.info(">>>>>>>>>>>,{},{}", userId, orderList);
                        // 结果集
                        Map<String, Object> result = new HashMap<>();
                        // touser
                        result.put("touser", userId);
                        // template_id
                        result.put("template_id", templateId);
                        // page 跳转页面
                        result.put("page", page);
                        // miniprogram_state
                        // TODO 正式上线之后需要注释掉，默认就是正式版
                        result.put("miniprogram_state", "developer");
                        // data
                        Map<String, Map<String, Object>> data = new HashMap<>();
                        // 下单时间
                        Map<String, Object> date4 = new HashMap<>();
                        date4.put("value",
                                // 由于一个人可能存在多个订单，则取时间最早的那个订单时间
                                orderList.parallelStream()
                                        .sorted((o1, o2) -> o1.getCreateDate().isBefore(o2.getCreateDate()) ? -1 : 1)
                                        .findFirst().get().getCreateDate());
                        data.put("date4", date4);
                        // 商品名称
                        Map<String, Object> thing6 = new HashMap<>();
                        thing6.put("value", orderInfoRepostory.getOrderCategoryByUserId(userId, startDateTime, endDateTime));
                        data.put("thing6", thing6);
                        // 采购数量
                        Map<String, Object> thing7 = new HashMap<>();
                        thing7.put("value", String.format("共 %s 斤"
                                , orderList.parallelStream().map(OrderInfoDO::getTotalWeight).reduce((o1, o2) -> o1 + o2).get()));
                        data.put("thing7", thing7);
                        // 订单内容
                        Map<String, Object> thing1 = new HashMap<>();
                        thing1.put("value", String.format("共%s笔订单，已支付%s笔，待支付金额为：%s"
                                , orderList.size()
                                , orderList.parallelStream().filter(info -> info.getStatus() == 1).count()
                                , orderList.parallelStream().map(OrderInfoDO::getTotalPrice).reduce((o1, o2) -> o1 + o2).get()));
                        data.put("thing1", thing1);
                        // 温馨提示
                        Map<String, Object> thing5 = new HashMap<>();
                        thing5.put("value", "点击进入小程序，查看详细订单信息。 \n 更能获取下次新订单通知 > ");
                        data.put("thing5", thing5);
                        result.put("data", data);

                    });
            // 发送完毕之后,更新已经处理的订单状态
            orderInfoRepostory.updateOrderNoticeFlag(StringUtils.join(orderInfoDOS.parallelStream().map(info -> info.getId()).collect(Collectors.toList()), ","));
        }
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
    @Scheduled(cron = "* * * * * ?")
    public void updateWechatAccessToken() {

    }
}
