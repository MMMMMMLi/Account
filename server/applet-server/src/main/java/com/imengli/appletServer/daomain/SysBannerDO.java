package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 首页Starter页面的轮播图
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysBannerDO {

    private int id;

    private String picUrlPre;

    private String picName;

    private String picUrl;

    private String status;

    private int orderType;
}
