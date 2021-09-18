package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.Data;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dto
 * 类名称:    ModuleInfoCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:49
 * 版本:      v1.0
 */
@Data
public class ModuleInfoCreateRequest {
    private String moduleCode;

    private String data;

    private String createUser;

    private String lastUpdateUser;
}
