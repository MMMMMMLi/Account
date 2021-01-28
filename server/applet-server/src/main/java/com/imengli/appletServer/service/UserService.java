/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 mmmmmengli@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.imengli.appletServer.service;


import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.dao.SysUserRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.SysUserDO;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.PinyinUtil;
import com.imengli.appletServer.utils.RedisUtil;
import com.imengli.appletServer.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private SysUserRepostory sysUserRepostory;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    public WechatAuthDO getWechatAuthEntity(String token) {
        WechatAuthDO wechatAuthDO = null;
        // 检验Token是否正常
        if (redisUtil.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthDO = redisUtil.getWechat(token);
        }
        return wechatAuthDO;
    }

    public ResultDTO authUserInfo(String token) {
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            WechatUserDO wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
            // 根据信息完善度返回
            if (wechatUserDOByOpenId != null) {
                if (StringUtils.isBlank(wechatUserDOByOpenId.getUserId())) {
                    // 需要完善
                    return new ResultDTO(ResultStatus.ERROR_UN_AUTHORIZED);
                } else {
                    SysUserDO info = sysUserRepostory.getUserInfoByUserId(wechatUserDOByOpenId.getUserId());
                    if (StringUtils.isAnyBlank(info.getAddress(), info.getUserName(), info.getPhoneNumber())) {
                        return new ResultDTO(ResultStatus.ERROR_USERINFO, info);
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
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            WechatUserDO wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
            if (wechatUserDOByOpenId != null) {
                String userId = wechatUserDOByOpenId.getUserId();
                if (StringUtils.isBlank(userId)) {
                    // 生成用户ID
                    String userID = String.valueOf(snowflakeIdWorker.nextId());
                    LocalDateTime now = LocalDateTime.now();
                    SysUserDO sysUserDO = new SysUserDO(userID, nickName, avatarUrl, gender, country, province, city, now, now, 0);
                    // 关联
                    wechatUserDOByOpenId.setUserId(userID);
                    // 保存
                    wechatUserRepostory.updateUserEntity(wechatUserDOByOpenId);
                    sysUserRepostory.save(sysUserDO);
                    return new ResultDTO(ResultStatus.SUCCESS_USERINFO, sysUserDO);
                } else {
                    return new ResultDTO(ResultStatus.SUCCESS_USERINFO, sysUserRepostory.getUserInfoByUserId(userId));
                }
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public ResultDTO updateWechatUserInfo(String token, String nickName, Integer gender, String city, String province, String country, String avatarUrl, String userInfoId) {
        // 校验Token
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 如果没有异常,则更新用户信息
            sysUserRepostory.update(
                    SysUserDO.builder()
                            .id(userInfoId)
                            .avatarUrl(avatarUrl)
                            .nickName(nickName)
                            .gender(gender)
                            .city(city)
                            .province(province)
                            .country(country)
                            .updateTime(LocalDateTime.now())
                            .build());
            return new ResultDTO(ResultStatus.SUCCESS, sysUserRepostory.getUserInfoByUserId(userInfoId));

        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public ResultDTO updateUserInfo(String token, String address, String name, String phone) {
        // 校验Token
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            WechatUserDO userEntityByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
            if (userEntityByOpenId != null) {
                // 更新
                sysUserRepostory.update(new SysUserDO(userEntityByOpenId.getUserId(), name, PinyinUtil.ToFirstChar(name), phone, address, LocalDateTime.now()));
                return new ResultDTO(ResultStatus.SUCCESS_UPDATE_USERINFO);
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }


    public ResultDTO getUserInfoBySearch(String token, String searchValue, Integer size) {
        // 校验Token
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            List<SysUserDO> userEntityList = sysUserRepostory.getUserInfoBySearch(searchValue, size);
            return new ResultDTO(ResultStatus.SUCCESS_SEARCH_USERINFO, userEntityList);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    public ResultDTO userSubMsgAdd(String userId) {
        // 获取当前用户的次数
        SysUserDO userInfoById = sysUserRepostory.getUserInfoByUserId(userId);
        // +1
        SysUserDO build = SysUserDO.builder().id(userInfoById.getId()).subMsgNum(userInfoById.getSubMsgNum() + 1).build();
        // 更新
        sysUserRepostory.update(build);
        // 返回新的
        userInfoById.setSubMsgNum(build.getSubMsgNum());
        return new ResultDTO(ResultStatus.SUCCESS, userInfoById);
    }

    /**
     * 添加临时用户
     *
     * @param token
     * @param userName
     * @param phoneNum
     * @param avatarUrl
     * @return
     */
    public ResultDTO addTempUserByApplet(String token, String userName, String phoneNum, String avatarUrl) {
        // 校验Token
        WechatAuthDO wechatAuthDO = this.getWechatAuthEntity(token);
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 首先判断当前添加的测试用户是否已经存在
            // 判断条件是名字是否存在或者手机号是否存在
            if(sysUserRepostory.getUserInfoBySearch(userName, 1).size() <= 0
                    && sysUserRepostory.getUserInfoBySearch(phoneNum, 1).size() <= 0) {
                // 如果都不存在,则添加
                sysUserRepostory.saveTempUser(SysUserDO.builder()
                        .id(String.valueOf(snowflakeIdWorker.nextId()))
                        .userName(userName)
                        .userNameCode(PinyinUtil.ToFirstChar(userName))
                        .phoneNumber(phoneNum)
                        .avatarUrl(avatarUrl)
                        .createTime(LocalDateTime.now())
                        .isTemp(Boolean.TRUE)
                        .build());
                return new ResultDTO(ResultStatus.SUCCESS);
            }else {
                return new ResultDTO(ResultStatus.EXISTED);
            }
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
