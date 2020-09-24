package com.imengli.userServer.service;

import com.imengli.userServer.dao.SysUserRepostory;
import com.imengli.userServer.dao.WechatUserRepostory;
import com.imengli.userServer.daomain.SysUserEntity;
import com.imengli.userServer.daomain.WechatAuthEntity;
import com.imengli.userServer.daomain.WechatUserEntity;
import com.imengli.userServer.dto.ResultDTO;
import com.imengli.userServer.dto.ResultStatus;
import com.imengli.userServer.remote.RedisWechatRemote;
import com.imengli.userServer.utils.PinyinUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisWechatRemote redisWechatRemote;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private SysUserRepostory sysUserRepostory;

    public WechatAuthEntity getWechatAuthEntity(String token) {
        WechatAuthEntity wechatAuthEntity = new WechatAuthEntity();
        // 检验Token是否正常
        if (redisWechatRemote.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthEntity = redisWechatRemote.getWechat(token);
        }
        return wechatAuthEntity;
    }

    public ResultDTO authUserInfo(String token) {
        WechatAuthEntity wechatAuthEntity = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthEntity != null) {
            // 获取对应的用户信息
            WechatUserEntity wechatUserEntityByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthEntity.getOpenId());
            // 根据信息完善度返回
            if (wechatUserEntityByOpenId != null) {
                if (StringUtils.isBlank(wechatUserEntityByOpenId.getUserId())) {
                    // 需要完善
                    return new ResultDTO(ResultStatus.ERROR_UN_AUTHORIZED);
                } else {
                    SysUserEntity info = sysUserRepostory.getUserInfoById(wechatUserEntityByOpenId.getUserId());
                    if(StringUtils.isAnyBlank(info.getAddress(),info.getUserName(),info.getPhoneNumber())) {
                        return new ResultDTO(ResultStatus.ERROR_USERINFO,info);
                    }
                    return new ResultDTO(ResultStatus.SUCCESS_USERINFO, info);
                }
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);

    }

    @Transactional
    public ResultDTO setUserInfo(String token, String nickName, Integer gender, String city, String province, String country, String avatarUrl) {
        // 校验Token
        WechatAuthEntity wechatAuthEntity = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthEntity != null) {
            // 获取对应的用户信息
            WechatUserEntity wechatUserEntityByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthEntity.getOpenId());
            if (wechatUserEntityByOpenId != null) {
                String userId = wechatUserEntityByOpenId.getUserId();
                if (StringUtils.isBlank(userId)) {
                    // 生成用户ID
                    String userID = UUID.randomUUID().toString();
                    SysUserEntity sysUserEntity = new SysUserEntity(userID, nickName, avatarUrl, gender, country, province, city);
                    // 关联
                    wechatUserEntityByOpenId.setUserId(userID);
                    // 保存
                    wechatUserRepostory.updateUserEntity(wechatUserEntityByOpenId);
                    sysUserRepostory.save(sysUserEntity);
                    return new ResultDTO(ResultStatus.SUCCESS_USERINFO, sysUserEntity);
                } else {
                    return new ResultDTO(ResultStatus.SUCCESS_USERINFO, sysUserRepostory.getUserInfoById(userId));
                }
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public ResultDTO updateUserInfo(String token, String address, String name, String phone) {
        // 校验Token
        WechatAuthEntity wechatAuthEntity = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthEntity != null) {
            WechatUserEntity userEntityByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthEntity.getOpenId());
            if (userEntityByOpenId != null) {
                // 更新
                sysUserRepostory.update(new SysUserEntity(userEntityByOpenId.getUserId(), name, PinyinUtil.ToFirstChar(name), phone, address));
                return new ResultDTO(ResultStatus.SUCCESS_UPDATE_USERINFO);
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }


    public ResultDTO getUserInfoBySearch(String token, String searchValue,Integer size) {
        // 校验Token
        WechatAuthEntity wechatAuthEntity = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthEntity != null) {
            List<SysUserEntity> userEntityList = sysUserRepostory.getUserInfoBySearch(searchValue,size);
            return new ResultDTO(ResultStatus.SUCCESS_SEARCH_USERINFO,userEntityList);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
