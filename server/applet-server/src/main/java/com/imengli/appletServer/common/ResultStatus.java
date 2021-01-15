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
    SUCCESS_USERINFO(20005,"获取用户信息成功"),
    SUCCESS_UPDATE_USERINFO(20006,"更新用户信息成功"),
    SUCCESS_SEARCH_USERINFO(20007,"搜索用户信息成功"),
    SUCCESS_COLLECTION(20008,"收款成功"),


    ERROR_GET_OPEN_ID(40001,"获取微信OpenId失败"),
    ERROR_AUTH_TOKEN(40002,"校验Token失败"),
    ERROR_USERINFO(40003,"用户信息不完整，请完善！"),
    ERROR_UN_AUTHORIZED(40004,"用户未授权！");

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
