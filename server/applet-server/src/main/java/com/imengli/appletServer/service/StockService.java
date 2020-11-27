package com.imengli.appletServer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imengli.appletServer.common.ResultStatus;
import com.imengli.appletServer.common.SystemEnum;
import com.imengli.appletServer.dao.StockRepostory;
import com.imengli.appletServer.daomain.StockInfoDO;
import com.imengli.appletServer.daomain.StockInfoDeatilDO;
import com.imengli.appletServer.daomain.WechatUserDO;
import com.imengli.appletServer.dto.ResultDTO;
import com.imengli.appletServer.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/11/27 9:13
 * @description: 库存管理
 * @version: v1
 */
@Service
public class StockService {

    private static final Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Resource
    private StockRepostory stockRepostory;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加库存操作详情
     *
     * @param stockInfoDeatil
     */
    public void addStockInfoDetail(StockInfoDeatilDO stockInfoDeatil) {
        stockRepostory.addStockInfoDetail(stockInfoDeatil);
    }

    /**
     * 添加库存
     *
     * @param stockKey 0: 货物 / 1: 框子
     * @param category
     * @param number
     * @return
     */
    @Transactional
    public ResultDTO insertStock(Integer stockKey, String category, Double number, String token) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            // 如果添加库存的标识为框子的话,则品种置为空
            if (stockKey == 1) {
                category = "";
            }
            // 查询当前库存相关库存的数量
            StockInfoDO stockInfoDO = stockRepostory.getStockInfoByKeyAndCategory(stockKey, category);
            // 如果已经存在,则修改库存量
            Double stockNumber = number;
            if (stockInfoDO != null) {
                stockNumber += stockInfoDO.getNumber();
            }
            // 当前时间
            LocalDateTime now = LocalDateTime.now();
            // 保存入库
            stockRepostory.insertStock(StockInfoDO.builder()
                    .key(stockKey)
                    .category(category)
                    .number(stockNumber)
                    .updateDate(now)
                    .updateBy(wechatUserDO.getUserId())
                    .build());
            // 保存操作日志入库
            this.addStockInfoDetail(StockInfoDeatilDO.builder()
                    .userId(wechatUserDO.getUserId())
                    .key(stockKey)
                    .category(category)
                    .number(number)
                    .type(SystemEnum.ADD.code())
                    .operationDate(now)
                    .operationBy(wechatUserDO.getUserId())
                    .build());
            return new ResultDTO(ResultStatus.SUCCESS);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }


    /**
     * 获取库存信息
     *
     * @param stockKey
     * @param token
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultDTO getStockInfo(Integer stockKey, String token, Integer pageNum, Integer pageSize) {
        // 校验token
        WechatUserDO wechatUserDO = redisUtil.getWechatAuthEntity(token);
        // 根据信息完善度返回
        if (wechatUserDO != null) {
            Map<String, Object> result = new HashMap<>();
            // 获取相关库存信息
            List<StockInfoDO> stockInfoDOList = stockRepostory.getStockInfoByKey(stockKey);
            // 获取操作详情
            PageHelper.startPage(pageNum, pageSize);
            List<Map<String,Object>> stockInfoDeatilDOList = stockRepostory.getStockInfoDetailByKey(stockKey);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(stockInfoDeatilDOList);

            // 库存信息
            result.put("stockInfo", stockInfoDOList);
            // 汇总信息
            result.put("stockSum", stockInfoDOList.parallelStream().map(info -> info.getNumber()).reduce((o1, o2) -> o1 + o2).get());
            // 操作详情
            result.put("stockInfoDeatil", pageInfo.getList());

            return new ResultDTO(ResultStatus.SUCCESS, result);
        }
        return new ResultDTO(ResultStatus.ERROR_AUTH_TOKEN);
    }
}
