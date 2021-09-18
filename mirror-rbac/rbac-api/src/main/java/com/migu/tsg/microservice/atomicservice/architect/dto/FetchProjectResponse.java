package com.migu.tsg.microservice.atomicservice.architect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: architect-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto <br>
* 类名称: FetchProjectListResponse.java <br>
* 类描述: 获取项目响应对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午5:27:37 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchProjectResponse {

    /**
     * 项目UUID
     */
    private String uuid;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 根账号
     */
    private String namespace;

    /**
     * 项目状态：pending，creating，success，deleting，error
     */
    private String status;

    /**
     * 项目创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * 项目更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;

}
