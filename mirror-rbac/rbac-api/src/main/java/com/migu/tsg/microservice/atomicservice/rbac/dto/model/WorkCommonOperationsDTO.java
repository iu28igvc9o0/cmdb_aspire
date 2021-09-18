package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
* 项目名称: rbac-api <br>
* 类描述: 服务台-常用操作类<br>
* 创建人: tongzhihong <br>
* 创建时间: 2020年09月16日下午3:12:59 <br>
* 版本: v1.0
*/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkCommonOperationsDTO {

    private String linkUrl;

    private String name;

    private String logo;

    private String isExternal;
}