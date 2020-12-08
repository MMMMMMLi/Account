package com.imengli.appletServer.dao;

import com.imengli.appletServer.common.SysConstant;
import com.imengli.appletServer.daomain.SysRoleDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: Weijia Jiang
 * @date: Created in 2020/12/8 11:01
 * @description:
 * @version: v1
 */
@Mapper
public interface SysRoleRepostory {

    @Select("select * from " + SysConstant.ROLE_TABLE_NAME + " where `id` = #{id} and useable = 1")
    SysRoleDo selectRoleInfoById(@Param("id") Integer id);
}
