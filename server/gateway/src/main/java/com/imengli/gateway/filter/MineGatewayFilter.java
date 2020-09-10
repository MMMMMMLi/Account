package com.imengli.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class MineGatewayFilter implements GlobalFilter, Ordered {

    @Value("${custom.unAuthPath}")
    private String unAuthPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 校验一下，不需要校验配置好的路径，其余都需要验证token的正确性
        if (!exchange.getRequest().getURI().getPath().contains(unAuthPath)) {
            // 获取token值
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (token == null || token.isEmpty()) {
                // 没有的话，直接返回401
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // 有的话，校验一下token的正确性

        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        // 值越小，则越先执行。
        return -100;
    }
}
