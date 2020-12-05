package com.imengli.appletServer.service;

import com.alibaba.fastjson.JSON;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.utils.HttpUtil;
import com.imengli.appletServer.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@Service
public class WechatAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(WechatAuthService.class);

    @Value("${applet.appID}")
    private String appID;

    @Value("${applet.appSecret}")
    private String appSecret;

    @Value("${applet.wechatAuthUri}")
    private String wechatAuthUri;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Autowired
    private RedisUtil redisUtil;

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
            openid = HttpUtil.getOpenid(authUrl);
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
}
