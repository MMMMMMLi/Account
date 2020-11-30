package com.imengli.appletServer.common;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:48
 * @description: 系统一些枚举
 * @version: v1
 */
public enum StockTypeEnum {

    ADD("add", "加操作"),
    REDUCE("reduce", "减操作"),
    ALIGN("align","校准操作");


    private String key;

    private String value;

    StockTypeEnum(String code, String value) {
        this.key = code;
        this.value = value;
    }

    public String key() {
        return this.key;
    }

    public String value() {
        return this.value;
    }
}
