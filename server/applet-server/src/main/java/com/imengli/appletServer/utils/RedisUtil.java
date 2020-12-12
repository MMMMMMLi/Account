package com.imengli.appletServer.utils;

import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, WechatAuthDO> wechatRedisTemplate;

    @Value("${mineRedis.userinfo.key}")
    private String KEY_PREFIX;

    // TODO: 正式环境的时候,修改一下超时时间。
    @Value("${mineRedis.default.timeout}")
    private Long DEFAULT_TIMEOUT;

    private TimeUnit timeUnit = TimeUnit.DAYS;

    // 校验Token的工具类

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    public WechatUserDO getWechatAuthEntity(String token) {
        WechatAuthDO wechatAuthDO = null;
        WechatUserDO wechatUserDOByOpenId = null;
        // 检验Token是否正常
        if (this.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthDO = this.getWechat(token);
        }
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
        }
        return wechatUserDOByOpenId;
    }


    // --------------------String : WechatAuthEntity ----------------------------------------

    public boolean setWechat(final String key, WechatAuthDO value) {
        return this.setWechat(key, value, DEFAULT_TIMEOUT);
    }

    /**
     * 写入redis
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     * @return 是否成功
     */
    public boolean setWechat(final String key, WechatAuthDO value, long time) {
        boolean result = false;
        try {
            wechatRedisTemplate.opsForValue().set(KEY_PREFIX + key, value);
            if (time > 0) {
                wechatRedisTemplate.expire(KEY_PREFIX + key, time, timeUnit);
            }
            result = true;
        } catch (Exception e) {
            log.error("写入缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }

    /**
     * 检查缓存是否存在
     *
     * @param key key
     * @return 是否存在
     */
    public boolean containsWechat(String key) {
        try {
            return wechatRedisTemplate.hasKey(KEY_PREFIX + key);
        } catch (Throwable e) {
            log.error("判断缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return false;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public WechatAuthDO getWechat(final String key) {
        try {
            return wechatRedisTemplate.opsForValue().get(KEY_PREFIX + key);
        } catch (Throwable e) {
            log.error("获取缓存失败，key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return null;
    }


    public boolean getAndSetWechat(final String key, WechatAuthDO value) {
        return this.getAndSetWechat(key, value, DEFAULT_TIMEOUT);
    }

    /**
     * 更新缓存
     */
    public boolean getAndSetWechat(final String key, WechatAuthDO value, long time) {
        boolean result = false;
        try {
            wechatRedisTemplate.opsForValue().getAndSet(KEY_PREFIX + key, value);
            if (time > 0) {
                wechatRedisTemplate.expire(KEY_PREFIX + key, time, timeUnit);
            }
            result = true;
        } catch (Exception e) {
            log.error("更新缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }

    /**
     * 删除缓存
     */
    public boolean deleteWechat(final String key) {
        boolean result = false;
        try {
            wechatRedisTemplate.delete(KEY_PREFIX + key);
            result = true;
        } catch (Exception e) {
            log.error("删除缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }


    // --------------------String : String -------------------------------------------

    /**
     * 写入redis
     */
    public boolean set(final String key, String value) {
        return this.set(key, value, DEFAULT_TIMEOUT);
    }

    /**
     * 写入redis
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     * @return 是否成功
     */
    public boolean set(final String key, String value, long time) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            result = true;
        } catch (Exception e) {
            log.error("写入缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }

    /**
     * 检查缓存是否存在
     *
     * @param key key
     * @return 是否存在
     */
    public boolean contains(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable e) {
            log.error("判断缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return false;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Throwable e) {
            log.error("获取缓存失败，key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return null;
    }

    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String value) {
        return this.getAndSet(key, value, DEFAULT_TIMEOUT);
    }

    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String value, long time) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            result = true;
        } catch (Exception e) {
            log.error("更新缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }

    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redisTemplate.delete(key);
            result = true;
        } catch (Exception e) {
            log.error("删除缓存失败,key:[{}],失败原因 Codeor:[{}]", key, e);
        }
        return result;
    }
}
