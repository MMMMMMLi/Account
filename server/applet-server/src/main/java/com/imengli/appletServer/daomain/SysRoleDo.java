package com.imengli.appletServer.daomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: Weijia Jian
 * @date: Created in 2020/12/8 10:55
 * @description: 用户权限表
 * @version: v1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDo {

    private Integer id;

    private String name;

    private String type;

    private Boolean useable;

    private LocalDateTime createDate;

    private String entryPage;

}
