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

package com.imengli.appletServer.common;

import com.imengli.appletServer.dao.SysConstantRepostory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/12 9:00
 * @description: 系统的一些常量
 * @version: v1
 */
@Component
public class SysConstant {

    // 常用的常量

    // 表名
    public static final String BANNERS_TABLE_NAME = "sys_banners";
    public static final String USER_TABLE_NAME = "sys_user";
    public static final String ROLE_TABLE_NAME = "sys_role";
    public static final String ORDER_INFO_TABLE_NAME = "order_info";
    public static final String ORDER_INFO_DEATIL_TABLE_NAME = "order_info_detail";
    public static final String CONSTANT_TABLE_NAME = "sys_constant";
    public static final String WECHAT_USER_TABLE_NAME = "wechat_user";
    public static final String STOCK_INFO_TABLE_NAME = "stock_info";
    public static final String STOCK_INFO_DEATIL_TABLE_NAME = "stock_info_detail";


    @Resource
    private SysConstantRepostory sysConstantRepostory;

    private static Map<Object, List<Map<String, Object>>> CONSTATN_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        // 获取内置的一些常量数据
        CONSTATN_MAP = sysConstantRepostory.getConstant()
                .parallelStream()
                .collect(Collectors.groupingBy(e -> e.get("type")));
    }

    public void update() {
        this.init();
    }

    /**
     * 获取指定type和key的数据
     *
     * @param type
     * @param keyValue
     * @return
     */
    public static Object getValueByTypeAndKey(String type, String keyValue) {
        List<Map<String, Object>> list = CONSTATN_MAP.get(type).
                parallelStream().filter(constant -> keyValue.equals(constant.get("key").toString())).collect(Collectors.toList());
        if (list.size() == 1) {
            return list.get(0).get("value").toString();
        } else if (list.size() > 1) {
            return list.parallelStream()
                    .sorted((o1, o2) -> o1.get("id").toString().compareTo(o2.get("id").toString()))
                    .map(o -> o.get("value"))
                    .collect(Collectors.toList());
        } else {
            return "";
        }
    }

    /**
     * 根据Type获取所有的数据
     *
     * @param type
     * @return
     */
    public static Map<String, List> getValuesByType(String type) {
        Map<String, List> result = new HashMap<>();
        CONSTATN_MAP.get(type)
                .parallelStream()
                .collect(Collectors.groupingBy(o -> o.get("key")))
                .forEach((k, v) -> {
                    result.put(
                            k.toString(),
                            v.parallelStream().map(o -> o.get("value")).collect(Collectors.toList())
                    );
                });
        return result;
    }
}
