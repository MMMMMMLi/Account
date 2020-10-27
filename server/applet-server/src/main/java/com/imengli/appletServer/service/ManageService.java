package com.imengli.appletServer.service;

import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.dao.ManageRepostory;
import com.imengli.appletServer.dao.WechatUserRepostory;
import com.imengli.appletServer.daomain.WechatAuthDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/22 15:26
 * @description: 管理相关Service
 * @version: v1
 */
@Service
public class ManageService {

    private final static Logger log = LoggerFactory.getLogger(ManageService.class);

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private WechatUserRepostory wechatUserRepostory;

    @Resource
    private ManageRepostory manageRepostory;

    public WechatUserDO getWechatAuthEntity(String token) {
        WechatAuthDO wechatAuthDO = null;
        WechatUserDO wechatUserDOByOpenId = null;
        // 检验Token是否正常
        if (redisUtil.containsWechat(token)) {
            // 获取Token对应的对象信息
            wechatAuthDO = redisUtil.getWechat(token);
        }
        // 判断是否异常
        if (wechatAuthDO != null) {
            // 获取对应的用户信息
            wechatUserDOByOpenId = wechatUserRepostory.getUserEntityByOpenId(wechatAuthDO.getOpenId());
        }
        return wechatUserDOByOpenId;
    }

    /**
     * 获取当日的订单汇总信息
     *
     * @param token
     * @return
     */
    public ResultDTO getSummaryOrderInfo(String token) {
        // 校验token
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // TODO: 后续添加管理员校验

            return new ResultDTO(ResultStatus.SUCCESS, manageRepostory.getSummaryOrderInfo());
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 根据不同的Type和State 获取报告
     *
     * @param token
     * @param type  category | size | person | time
     * @return
     */
    public ResultDTO getReportByDay(String token, String type) {
        // 校验token
        WechatUserDO wechatUserDO = this.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // TODO: 后续添加管理员校验

            // 设置起始时间
            LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
            // 结束时间都是当前时间
            LocalDateTime endTime = LocalDateTime.now();
            // 根据不同的 Type类型来获取数据
            // 返回小程序端的数据结构:
            // legend_data: 导航名字
            // xAxis_data:  X栏数据
            // series[  // 是否存在多个   导航名字
            //      {
            //          name: 导航名字[0],
            //          data: 数据
            //      }....
            // ]
            Map<String, Object> result = new HashMap<>();
            switch (type) {
                // 各品种销量情况
                case "category":
                    // 获取数据
                    List<Map<String, Object>> reportByCategory = manageRepostory.getReportByCategory(startTime, endTime);
                    if (reportByCategory.size() > 0) {
                        // 拼装数据
                        // legend_data
                        assembleData(result, reportByCategory, "category");
                    }
                    break;
                // 各型号销量情况
                case "size":
                    // 获取数据
                    List<Map<String, Object>> reportBySize = manageRepostory.getReportBySize(startTime, endTime);
                    if (reportBySize.size() > 0) {
                        // 拼装数据
                        assembleData(result, reportBySize, "size");
                    }
                    break;
                // 成交量情况
                case "person":
                    // 成交量排行,只看当天的即可。
                    startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    // 获取数据
                    List<Map<String, Object>> reportByPerson = manageRepostory.getReportByPerson(startTime, endTime);
                    if (reportByPerson.size() > 0) {
                        // 拼装数据
                        // xAxis_data
                        result.put("xAxis_data", reportByPerson.stream().map(info -> info.get("userName")).collect(Collectors.toList()));
                        // series
                        List<Object> seriesInfoList = new ArrayList<>();
                        Map<String, Object> seriesInfo = new HashMap<>();
                        // 这个只有一个柱状图,所有表头提示就省略掉了.
                        seriesInfo.put("name", "");
                        Map<String, Object> totalPriceInfo = new HashMap<>();
                        seriesInfo.put("data", reportByPerson.stream().map(info -> info.get("totalPrice")).collect(Collectors.toList()));

                        seriesInfoList.add(seriesInfo);

                        result.put("series", seriesInfoList);
                    }
                    break;
                // 各个时间段的交易情况
                case "time":
                    // 时间段排行,只看当天的即可。
                    startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    // 获取数据
                    List<Map<String, Object>> reportByTime = manageRepostory.getReportByTime(startTime, endTime);
                    if (reportByTime.size() > 0) {
                        // 拼装数据
                        // legend_data
                        List<String> legendDataList = Arrays.asList("价钱/元", "重量/KG");
                        result.put("legend_data", legendDataList);
                        // List<String> timeList = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
                        List<Object> timeList = reportByTime.stream().map(report -> report.get("dateTime")).collect(Collectors.toList());
                        // xAxis_data
                        result.put("xAxis_data", timeList);
                        // series
                        List<Object> seriesList = new ArrayList<>();
                        legendDataList.stream().forEach(k -> {
                            Map<String, Object> infos = new HashMap<>();
                            infos.put("name", k);
                            infos.put("data", timeList.stream().map(time -> {
                                List<Object> collect = reportByTime.stream()
                                        .filter(report -> time.equals(report.get("dateTime").toString()))
                                        .map(report -> {
                                            String flag = "sumPrice";
                                            if (k.equals("重量/KG")) {
                                                flag = "sumWeight";
                                            }
                                            return report.getOrDefault(flag, 0);
                                        }).collect(Collectors.toList());
                                if(collect.size() > 0) {
                                    return collect.get(0);
                                }
                                return 0;
                            }).collect(Collectors.toList()));
                            seriesList.add(infos);
                        });
                        result.put("series", seriesList);
                    }
                    break;
                default:
                    break;
            }
            if (result.size() > 0) {
                return new ResultDTO(ResultStatus.SUCCESS, result);
            }
            return new ResultDTO(ResultStatus.NULL, result);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }

    /**
     * 根据不同的参数组装 前端 图标的 数据信息
     *
     * @param result
     * @param reportBySize
     * @param type
     */
    private void assembleData(Map<String, Object> result, List<Map<String, Object>> reportBySize, String type) {
        // legend_data
        result.put("legend_data", reportBySize.parallelStream().map(info -> info.get("dateTime")).collect(Collectors.toSet()));
        // xAxis_data
        result.put("xAxis_data", SysConstant.getValueByTypeAndKey("web", type));
        // series
        reportBySize
                .parallelStream()
                .collect(Collectors.groupingBy(e -> e.get("dateTime")))
                .forEach((k, v) -> {
                    List<Object> series = new ArrayList<>();
                    if (result.containsKey("series")) {
                        series = (ArrayList) result.get("series");
                    }
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", k);
                    info.put("data",
                            ((ArrayList) SysConstant.getValueByTypeAndKey("web", type))
                                    .stream()
                                    .map(value -> {
                                        List<Object> weight = v.parallelStream()
                                                .filter(vInfo -> vInfo.get(type).equals(value))
                                                .map(vInfo -> vInfo.get("sumWeight"))
                                                .collect(Collectors.toList());
                                        if (weight.size() > 0) {
                                            return weight.get(0);
                                        }
                                        return 0;
                                    }).collect(Collectors.toList()));
                    series.add(info);
                    result.put("series", series);
                });
    }
}
