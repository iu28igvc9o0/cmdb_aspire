package com.migu.tsg.microservice.atomicservice.composite.service.dict;

import lombok.Data;

/**
 * 字典业务层实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.api.dto.model
 * 类名称:    DictDTO.java
 * 类描述:    字典业务层实体
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 17:45
 * 版本:      v1.0
 */
@Data
public class DictDTO {
    private String code;

    private String desc;

    private String result;
}
