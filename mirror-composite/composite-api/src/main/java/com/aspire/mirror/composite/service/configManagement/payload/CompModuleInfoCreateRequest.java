package com.aspire.mirror.composite.service.configManagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.configManagement.payload
 * 类名称:    CompModuleInfoCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 17:05
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompModuleInfoCreateRequest {
    private String moduleCode;
    private String data;
}
