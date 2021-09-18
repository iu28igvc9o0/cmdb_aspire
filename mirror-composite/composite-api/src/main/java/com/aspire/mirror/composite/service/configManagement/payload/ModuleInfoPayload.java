package com.aspire.mirror.composite.service.configManagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.configManagement.payload
 * 类名称:    ModuleInfoPayload.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 15:29
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleInfoPayload {
    private String moduleCode;
    private String data;

    private String id;

    private String createUser;

    private String lastUpdateUser;

    private Date createTime;

    private Date updateTime;
}
