package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/10/12 9:01
 * @description: 获取系统常量
 * @version: v1
 */
@Mapper
public interface SysConstantRepostory {

    @Select("select type,`key`,`value`,sortFlag from " + SysConstant.CONSTANT_TABLE_NAME + " where status = 1 order by type,`key`,sortFlag desc")
    List<Map<String, Object>> getConstant();
}
