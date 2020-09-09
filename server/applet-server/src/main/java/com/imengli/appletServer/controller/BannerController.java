package com.imengli.appletServer.controller;

import com.imengli.appletServer.dao.BannerRepostory;
import com.imengli.appletServer.daomain.BannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BannerController {

    @Autowired
    private BannerRepostory bannerRepostory;

    @RequestMapping("/getBanners")
    public List<BannerEntity> getBanners() {
        return bannerRepostory.getBanners();
    }

}
