package com.imengli.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallBackController {

    @RequestMapping("/fallback")
    public Map<String, Object> fallBack() {
        Map<String, Object> result = new HashMap();
        result.put("code","40000");
        result.put("msg","服务出错，请联系管理员。");
        return result;
    }
}
