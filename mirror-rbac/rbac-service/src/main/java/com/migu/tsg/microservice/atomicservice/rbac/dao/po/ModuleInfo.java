package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dao.po
 * 类名称:    ModuleInfo.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:29
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleInfo {
    /**
     * 模块编码
     */
    private String moduleCode;
    /**
     * 模块数据，现只有组件数据
     */
    private String data;

    private String id;

    private String createUser;

    private String lastUpdateUser;

    private Date createTime;

    private Date updateTime;

}
