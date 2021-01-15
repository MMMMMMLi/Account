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

package com.imengli.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MineGatewayFilter implements GlobalFilter, Ordered {

    @Value("${custom.unAuthPath}")
    private String unAuthPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 校验一下，不需要校验配置好的路径，其余都需要验证token的正确性
        if (Arrays.asList(unAuthPath.split(","))
                .stream()
                .filter(path -> exchange.getRequest().getURI().getPath().contains(path))
                .collect(Collectors.toList())
                .size() == 0) {
            // 获取token值
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (token == null || token.isEmpty()) {
                // 没有的话，直接返回401
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // 有的话，校验一下token的正确性
            // TODO: 原本打算在这个地方同意校验方法的Token实用性,但是由于线程阻塞的缘故,调增到每个服务单独校验. By:2020-09-11 13:27:31
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        // 值越小，则越先执行。
        return -100;
    }
}
