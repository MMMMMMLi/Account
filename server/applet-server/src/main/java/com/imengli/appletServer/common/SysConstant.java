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
                    .sorted((o1, o2) -> o2.get("sortFlag").toString().compareTo(o1.get("sortFlag").toString()))
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
