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

package com.imengli.appletServer.service;

import com.imengli.appletServer.dao.AppletRepostory;
import com.imengli.appletServer.daomain.SysBannerDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.common.ResultStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppletService {

    @Resource
    private AppletRepostory appletRepostory;

    public ResultDTO<List<AppletRepostory>> getBanners() {
        List<SysBannerDO> sysBannerDOList = appletRepostory.getBanners();
        if(sysBannerDOList.size() == 0) {
            return new ResultDTO(ResultStatus.SUCCESS_SELECT_NOSHOW);
        }
        return new ResultDTO(ResultStatus.SUCCESS_SELECT_SHOW, sysBannerDOList);
    }
}
