package com.imengli.appletServer.service;

import com.alibaba.fastjson.JSON;
import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.dao.OrderInfoRepostory;
import com.imengli.appletServer.dao.SysUserRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.*;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.HttpUtil;
import com.imengli.appletServer.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class WechatAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(WechatAuthService.class);

    @Value("${applet.appID}")
    private String appID;

    @Value("${applet.appSecret}")
    private String appSecret;

    @Value("${applet.wechatAuthUri}")
    private String wechatAuthUri;

    @Value("${applet.templateId}")
    private String templateId;

    @Value("${applet.page}")
    private String page;

    @Value("${applet.sendMsgUri}")
    private String sendMsgUri;

    @Value("${applet.wechatTokenKey}")
    private String wechatTokenKey;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private OrderInfoRepostory orderInfoRepostory;

    @Resource
    private SysUserRepostory sysUserRepostory;

    @Autowired
    private RedisUtil redisUtil;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据用户Code获取当前登陆用户信息
     *
     * @param code 用户登陆时候,微信给返回的唯一code码
     * @return 结果集
     */
    public ResultDTO codeToSession(String code, String token) {
        // 每次登陆之前，需要先判断之前是否存在登陆token
        if (StringUtils.isNotBlank(token)) {
            // 查看是否存在此token
            if (redisUtil.containsWechat(token)) {
                // 存在，则需要先删除。
                redisUtil.deleteWechat(token);
            }
        }

        WechatUserDO wechatUserDO = new WechatUserDO();
        // 拼接url
        String authUrl = String.format(wechatAuthUri, appID, appSecret, code);
        // 调用微信的Auto接口,获取当前用户信息.
        String openid = null;
        try {
            openid = HttpUtil.execLink(authUrl);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return new ResultDTO(ResultStatus.ERROR_GET_OPEN_ID);
        }
        // 格式化为User对象
        WechatAuthDO wechatAuthDO = JSON.parseObject(openid, WechatAuthDO.class);
        // 判断是否获取正常
        if (StringUtils.isAllEmpty(wechatAuthDO.getErrcode(), wechatAuthDO.getErrmsg())
                && StringUtils.isNoneEmpty(wechatAuthDO.getOpenId(), wechatAuthDO.getSession_key())) {
            // 获取openId
            String openId = wechatAuthDO.getOpenId();
            // 如果获取正常,先判断当前用户是否登陆过
            wechatUserDO = wechatUserRepostory.getUserEntityByOpenId(openId);
            if (wechatUserDO == null) {
                // 用户之前没有登陆过,则添加用户并继续获取用户详情信息
                wechatUserDO = new WechatUserDO(openId, wechatAuthDO.getUnionId());
                // 保存一下基本信息
                wechatUserRepostory.saveUserEntity(wechatUserDO);
            }
            // 更新好用户信息之后,返回保存Token值到Redis然后返回给微信小程序。
            // TODO: 此处生成Token的方法过于简单，后续升级一下。
            String uuidToken = UUID.randomUUID().toString();
            // 2020-12-12 update 将token的过期时间设置为永不过期。
            // 2020-12-12 又更新,还是得设置过期时间,要不然Redis会堆积大量垃圾Token占用内存,现在配置的为 15Days
            redisUtil.setWechat(uuidToken, wechatAuthDO);
            return new ResultDTO(ResultStatus.SUCCESS_LOGIN, uuidToken);
        } else {
            return new ResultDTO(Integer.valueOf(wechatAuthDO.getErrcode()), wechatAuthDO.getErrmsg());
        }
    }

    public ResultDTO<String> checkToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            WechatAuthDO entity = redisUtil.getWechat(token);
            if (entity != null) {
                return new ResultDTO(ResultStatus.SUCCESS_AUTH_TOKEN);
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 推送订单信息
     *
     * @param orderInfoDOS
     */
    public Integer sendMsgToWechat(List<OrderInfoDO> orderInfoDOS) {
        LOG.info(">>>>>> 开始推送订单消息。");
        // 获取系统在微信端的Token
        String nowWechatToken = redisUtil.get(wechatTokenKey);
        // 构建推送请求的url
        String url = String.format(sendMsgUri, nowWechatToken);
        // 失败的用户List
        CopyOnWriteArrayList<String> errorUserIds = new CopyOnWriteArrayList<String>();
        // 迭代订单列表进行发送
        orderInfoDOS.parallelStream()
                // 过滤已经发送过的订单信息
                .filter(info -> !info.getIsNotice())
                // 根据用户ID分组
                .collect(Collectors.groupingBy(info -> info.getUserId()))
                // 迭代通知
                .forEach((userId, orderList) -> {
                    // 首先看看当前用户是不是手动添加的用户
                    Optional<WechatUserDO> userEntityByUserId = Optional.ofNullable(wechatUserRepostory.getUserEntityByUserId(userId));
                    if(userEntityByUserId.isPresent()) {
                        // 如果不是则,推送消息
                        // 结果集
                        Map<String, Object> result = new HashMap<>();
                        // touser
                        result.put("touser", userEntityByUserId.get().getOpenId());
                        // template_id
                        result.put("template_id", templateId);
                        // page 跳转页面
                        result.put("page", page);
                        // miniprogram_state
                        // TODO 正式上线之后需要注释掉，默认就是正式版
                        result.put("miniprogram_state", "trial");
                        // data
                        Map<String, Map> data = new HashMap<>();
                        // 下单时间
                        Map<String, Object> date4 = new HashMap<>();
                        date4.put("value",
                                // 由于一个人可能存在多个订单，则取时间最早的那个订单时间
                                dateTimeFormatter.format(orderList.parallelStream()
                                        .sorted((o1, o2) -> o1.getCreateDate().isBefore(o2.getCreateDate()) ? -1 : 1)
                                        .findFirst().get().getCreateDate()));
                        data.put("date4", date4);
                        // 商品名称
                        Map<String, Object> thing6 = new HashMap<>();
                        List<OrderInfoDetailDO> orderInfoDetailDOS = orderInfoRepostory.getOrderInfoDetailsByOrderId(
                                StringUtils.join(orderList.stream().map(OrderInfoDO::getId).collect(Collectors.toList()), ",")
                        );
                        thing6.put("value", StringUtils.join(
                                orderInfoDetailDOS.parallelStream().map(OrderInfoDetailDO::getCategoryValue).distinct().collect(Collectors.toList()),
                                "，"));
                        data.put("thing6", thing6);
                        // 采购数量
                        Map<String, Object> thing7 = new HashMap<>();
                        thing7.put("value", String.format("%s斤，待付：%s元"
                                , orderList.parallelStream().map(OrderInfoDO::getTotalWeight).reduce((o1, o2) -> o1 + o2).get()
                                , orderList.parallelStream().map(OrderInfoDO::getTotalPrice).reduce((o1, o2) -> o1 + o2).get()));
                        data.put("thing7", thing7);
                        // 订单内容
                        Map<String, String> thing1 = new HashMap<>();
                        thing1.put("value", String.format("共%s笔订单，已支付%s笔"
                                , orderList.size()
                                , orderList.parallelStream().filter(info -> info.getStatus() == 1).count()
                        ));
                        data.put("thing1", thing1);
                        // 温馨提示
                        Map<String, Object> thing5 = new HashMap<>();
                        thing5.put("value", "点击进入小程序，查看详细订单信息");
                        data.put("thing5", thing5);
                        result.put("data", data);

                        // 构建请求集完毕，开始执行推送程序。
                        Map httpResult = JSON.parseObject(HttpUtil.sendRequest(url, result, HttpMethod.POST), Map.class);
                        String errmsg = String.valueOf(httpResult.get("errmsg"));
                        if ("ok".equals(errmsg)) {
                            LOG.info(">>>>>> 发送成功！");
                            // 发送成功之后更新用户表
                            // 更新
                            sysUserRepostory.update(
                                    SysUserDO.builder()
                                            .id(userId)
                                            .subMsgNum(sysUserRepostory.getUserInfoById(userId).getSubMsgNum() - 1)
                                            .build());
                        } else {
                            LOG.info(">>>>>> 发送失败！,{}",errmsg);
                            errorUserIds.add(userId);
                        }
                    }else {
                        LOG.info(">>>>>> 发送失败！当前用户为手动添加用户，无法推送。");
                        errorUserIds.add(userId);
                    }
                });
        List<Integer> updateOrderId = orderInfoDOS.parallelStream().
                // 过滤掉发送失败的用户订单信息
                        filter(info -> !errorUserIds.contains(info.getUserId()))
                .map(info -> info.getId())
                .collect(Collectors.toList());
        if (updateOrderId.size() > 0) {
            // 发送完毕之后,更新已经处理的订单状态
            orderInfoRepostory.updateOrderNoticeFlag(
                    StringUtils.join(updateOrderId, ",")
            );
        }
        return updateOrderId.size();
    }
}
