package com.imengli.appletServer.schedule;

import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.daomain.OrderInfoDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Value("${applet.templateId}")
    private static String templateId;

    @Value("${applet.page}")
    private static String page;

    /**
     * 每天定时推送订单信息
     */
    @Scheduled(cron = "* * * * * ?")
    public void subMessage() {
        LOG.info(">>>>>> 开始执行消息推送。");
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        // 获取今天的所有订单
        List<OrderInfoDO> orderInfoDOS = orderInfoRepostory.selectAllOrderList(
                startDateTime, endDateTime, new ArrayList<>());
        // 获取系统的Token

        // 迭代订单列表进行发送
        orderInfoDOS.parallelStream()
                .collect(Collectors.groupingBy(info -> info.getUserId()))
                .forEach((userId, orderList) -> {
                    LOG.info(">>>>>>>>>>>,{},{}", userId, orderList);
                    // 结果集
                    Map<String,Object> result = new HashMap<>();
                    // touser
                    result.put("touser",userId);
                    // template_id
                    result.put("template_id",templateId);
                    // page 跳转页面
                    result.put("page",page);
                    // miniprogram_state
                    // TODO 正式上线之后需要注释掉，默认就是正式版
                    result.put("miniprogram_state","developer");
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
                    thing6.put("value", orderInfoRepostory.getOrderCategoryByUserId(userId, startDateTime,endDateTime));
                    data.put("thing6", thing6);
                    // 采购数量
                    Map<String, Object> thing7 = new HashMap<>();
                    thing7.put("value",String.format("共 %s 斤"
                            ,orderList.parallelStream().map(OrderInfoDO::getTotalWeight).reduce((o1,o2) -> o1 + o2).get()));
                    data.put("thing7", thing7);
                    // 订单内容
                    Map<String, Object> thing1 = new HashMap<>();
                    thing1.put("value",String.format("共%s笔订单，累计金额为：%s"
                            ,orderList.size()
                            ,orderList.parallelStream().map(OrderInfoDO::getTotalPrice).reduce((o1,o2) -> o1 + o2).get()));
                    data.put("thing1", thing1);
                    // 温馨提示
                    Map<String, Object> thing5 = new HashMap<>();
                    thing5.put("value","点击进入小程序，查看详细订单信息。 \n 更能获取下次新订单通知 > ");
                    data.put("thing5", thing5);
                    result.put("data",data);

                });
    }
}
