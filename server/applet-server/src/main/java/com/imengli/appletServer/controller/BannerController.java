package com.imengli.appletServer.controller;

import com.imengli.appletServer.dao.BannerRepostory;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.service.BanneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applet")
public class BannerController {

    @Autowired
    private BanneService banneService;

    @RequestMapping("/getBanners")
    public ResultDTO<List<BannerRepostory>> getBanners() {
        return banneService.getBanners();
    }

}
