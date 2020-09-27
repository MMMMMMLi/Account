package com.imengli.appletServer.service;

import com.imengli.appletServer.dao.BannerRepostory;
import com.imengli.appletServer.daomain.SysBannerDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.common.ResultStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BanneService {

    @Resource
    private BannerRepostory bannerRepostory;

    public ResultDTO<List<BannerRepostory>> getBanners() {
        List<SysBannerDO> sysBannerDOList = bannerRepostory.getBanners();
        if(sysBannerDOList.size() == 0) {
            return new ResultDTO(ResultStatus.SUCCESS_SELECT_NOSHOW);
        }
        return new ResultDTO(ResultStatus.SUCCESS_SELECT_SHOW, sysBannerDOList);
    }
}
