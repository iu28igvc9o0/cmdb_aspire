package com.migu.tsg.microservice.atomicservice.rbac.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dto.vo
 * 类名称:    ModuleInfoVO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:03
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfoVO {
    private String moduleCode;

    private String data;

    private String id;

    private String createUser;

    private String lastUpdateUser;

    private Date createTime;

    private Date updateTime;
}
