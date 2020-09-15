package com.imengli.userServer.service;

import com.imengli.userServer.dao.SysUserRepostory;
import com.imengli.userServer.dao.WechatUserRepostory;
import com.imengli.userServer.daomain.WechatAuthEntity;
import com.imengli.userServer.daomain.WechatUserEntity;
import com.imengli.userServer.dto.ResultDTO;
import com.imengli.userServer.dto.ResultStatus;
import com.imengli.userServer.remote.RedisWechatRemote;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisWechatRemote redisWechatRemote;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private SysUserRepostory sysUserRepostory;

    public ResultDTO authUserInfo(String token) {
        WechatAuthEntity wechatAuthEntity = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthEntity != null) {
            // 获取对应的用户信息
            WechatUserEntity wechatUserEntityByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthEntity.getOpenId());
            // 根据信息完善度返回
            if (StringUtils.isBlank(wechatUserEntityByOpenId.getUserId())) {
                // 需要完善
                return new ResultDTO(ResultStatus.ERROR_USERINFO);
            } else {
                return new ResultDTO(ResultStatus.SUCCESS_USERINFO, sysUserRepostory.getUserInfoById(wechatUserEntityByOpenId.getUserId()));
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);

    }

    /**
     * 根据Token值获取wechatAuthEntity
     *
     * @param token yonghuToken
     * @return wechatAuthEntity
     */
    private WechatAuthEntity getWechatAuthEntity(String token) {
        WechatAuthEntity wechatAuthEntity = new WechatAuthEntity();
        // 检验Token是否正常
        if (redisWechatRemote.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthEntity = redisWechatRemote.getWechat(token);
        }
        return wechatAuthEntity;
    }
}
