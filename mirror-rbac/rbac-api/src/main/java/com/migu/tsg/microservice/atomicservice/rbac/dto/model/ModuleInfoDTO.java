package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dto.model
 * 类名称:    ModuleInfoDTO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:12
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleInfoDTO {
    /**
     * 模块编码
     */
    private String moduleCode;
    /**
     * 模块数据，当前只有组件数据
     */
    private String data;


    private String id;

    private String createUser;

    private String lastUpdateUser;

    private Date createTime;

    private Date updateTime;
}
