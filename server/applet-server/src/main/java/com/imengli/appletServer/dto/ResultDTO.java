package com.imengli.appletServer.dto;

import com.github.pagehelper.PageInfo;
import com.imengli.appletServer.common.ResultStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 7434727470152168503L;

    private int code;

    private String msg;

    private T data;

    private PageInfo pageInfo;

    /**
     * @param resultStatus 通用状态
     */
    public ResultDTO(ResultStatus resultStatus) {
        this(resultStatus.code(), resultStatus.msg(), null);
    }

    /**
     * @param code 提示状态码
     * @param msg  提示内容
     */
    public ResultDTO(int code, String msg) {
        this(code, msg, null);
    }

    /**
     * @param resultStatus 通用状态
     * @param data         数据内容
     */
    public ResultDTO(ResultStatus resultStatus, T data) {
        this(resultStatus.code(), resultStatus.msg(), data);
    }

    public ResultDTO(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public ResultDTO(ResultStatus resultStatus, T data, PageInfo pageInfo) {
        this(resultStatus.code(), resultStatus.msg(), data, pageInfo);
    }

    public ResultDTO(int code, String msg, T data, PageInfo pageInfo) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.pageInfo = pageInfo;
    }
}
