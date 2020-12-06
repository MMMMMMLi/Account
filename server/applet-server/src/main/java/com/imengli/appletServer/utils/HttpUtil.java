package com.imengli.appletServer.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpUtil {

    public static String execLink(String url) throws IOException {

        BufferedReader in = null;
        //appid和secret是开发者分别是小程序ID和小程序密钥，开发者通过微信公众平台-》设置-》开发设置就可以直接获取，
        try {
            URL Url = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = Url.openConnection();
            // 设置通用的请求属性
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendRequest(String url, Map<String, Object> param, HttpMethod method) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        //设置超时时间，请求时间，连接时间，读取时间，都是5秒
        httpRequestFactory.setConnectionRequestTimeout(5000);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);
        //获取对话
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        //JSON contentType
        headers.setContentType(MediaType.APPLICATION_JSON);
        //我们发起 HTTP 请求还是最好加上"Connection","close" ，有利于程序的健壮性
        headers.set("Connection", "close");
        HttpEntity<Map> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> response;
        try {
            //发起请求
            response = restTemplate.exchange(
                    url,
                    method,//get
                    requestEntity,
                    String.class);
            //获取返回值
            String resTxt = response.getBody();
            if (StringUtils.isNotBlank(resTxt)) {
//                Map<String, Object> map = JSON.parseObject(resTxt, Map.class);
                /**
                 * 返回值
                 * {"result":0,"content":"曲项向天歌"}
                 * result 在反转对象的时候，会变成 Double 类型，是0.0
                 */
                return resTxt;
            }
        } catch (RestClientException e) {
            throw e;
        }
        return null;
    }
}
