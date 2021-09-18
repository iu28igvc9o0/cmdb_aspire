package com.migu.tsg.microservice.atomicservice.architect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: architect-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto <br>
* 类名称: CreateProjectRequest.java <br>
* 类描述: 创建单个项目的请求对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午6:06:11 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectRequest {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目模板名称
     */
    private String template;

    /**
     * 根账号
     */
    private String namespace;

}
