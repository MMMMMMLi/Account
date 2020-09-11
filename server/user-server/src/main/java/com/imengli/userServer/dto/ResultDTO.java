package com.imengli.userServer.dto;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;

public class ResultDTO<T> implements Serializable {

    private int code;

    private String msg;

    private PageInfo page;

    private T data;

    public ResultDTO() {}

    /**
     * @param resultStatus 通用状态
     */
    public ResultDTO(ResultStatus resultStatus) {
        this(resultStatus.code(), resultStatus.msg(), null, null);
    }

    /**
     * @param code 提示状态码
     * @param msg 提示内容
     */
    public ResultDTO(int code, String msg) {
        this(code, msg, null, null);
    }

    /**
     * @param resultStatus 通用状态
     * @param data 数据内容
     */
    public ResultDTO(ResultStatus resultStatus, T data) {
        this(resultStatus.code(), resultStatus.msg(), null, data);
    }

    /**
     * @param code 提示状态码
     * @param msg 提示内容
     * @param data 数据内容
     */
    public ResultDTO(int code, String msg, T data) {
        this(code, msg, null, data);
    }

    /**
     * @param resultStatus 通用状态
     * @param page 分页信息
     * @param data 数据内容
     */
    public ResultDTO(ResultStatus resultStatus, PageInfo page, T data) {
        this(resultStatus.code(), resultStatus.msg(), page, data);
    }

    /**
     * @param code 提示状态码
     * @param msg 提示内容
     * @param page 分页信息
     * @param data 数据内容
     */
    public ResultDTO(int code, String msg, PageInfo page, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.page = page;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public ResultDTO<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultDTO<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public PageInfo getPage() {
        return page;
    }

    public ResultDTO<T> setPage(PageInfo page) {
        this.page = page;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultDTO<T> setData(T data) {
        this.data = data;
        return this;
    }

}
