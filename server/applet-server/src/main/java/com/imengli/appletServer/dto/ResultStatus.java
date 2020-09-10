package com.imengli.appletServer.dto;

public enum ResultStatus {

    NULL(10000, "空值"),
    SUCCESS(20000, "成功"),
    WRONG(30000, "失败"),
    ERROR(40000, "错误"),
    EXISTED(50000, "已存在"),


    SUCCESS_SELECT_NOSHOW(20001,"查询成功,但不展示"),
    SUCCESS_SELECT_SHOW(20002,"查询成功,并展示"),
    SUCCESS_AUTH_TOKEN(20003,"校验Token成功"),
    SUCCESS_LOGIN(20004,"登陆成功"),


    ERROR_GET_OPEN_ID(40001,"获取微信OpenId失败"),
    ERROR_AUTH_TOKEN(40002,"校验Token失败");

    // 状态码
    private Integer code;
    // 状态描述
    private String msg;

    private ResultStatus(Integer code, String msg) {
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
}