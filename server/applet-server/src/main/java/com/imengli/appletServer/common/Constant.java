package com.imengli.appletServer.common;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/9/28 16:07
 * @description: 常用的常量枚举类
 * @version: v1
 */
public enum Constant {

    UNPAID(0,"待付款"),
    PAID(1,"已付款");


    // 状态码
    private Integer code;
    // 状态描述
    private String msg;

    private Constant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取状态码
     */
    public Integer code() {
        return code;
    }

    /**
     * 获取状态描述
     */
    public String msg() {
        return msg;
    }

    // 普通方法
    public static String getMsg(int code) {
        for (Constant c : Constant.values()) {
            if (c.code() == code) {
                return c.msg;
            }
        }
        return null;
    }
}
