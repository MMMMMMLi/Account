package com.imengli.appletServer.common;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:48
 * @description: 系统一些枚举
 * @version: v1
 */
public enum SystemEnum {

    ADD(0, "加操作"),
    REDUCE(1, "减操作");


    private Integer code;

    private String value;

    SystemEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer code() {
        return this.code;
    }

    public String value() {
        return this.value;
    }
}
