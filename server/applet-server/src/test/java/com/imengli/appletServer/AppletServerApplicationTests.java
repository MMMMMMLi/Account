package com.imengli.appletServer;

import com.imengli.appletServer.dao.SysUserRepostory;
import com.imengli.appletServer.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AppletServerApplicationTests {

    @Resource
    private SysUserRepostory sysUserRepostory;


    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
//        System.out.println(sysUserRepostory.getUserInfoById("565793f0-9051-452a-a885-3b65897c3cd7"));
        // 测试redis过期时间设置
        redisUtil.set("testTime","Hello redis",-999);
    }

}
