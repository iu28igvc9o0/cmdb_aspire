package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: ResourceDTO.java <br>
 * 类描述: 新增角色权限,返回资源信息 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午9:52:46<br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceDTO {

    /**
     * 资源名
     */
    @JsonProperty("resource_name")
    private String resourceName;

    /**
     * 资源类型
     */
    @JsonProperty("resource_type")
    private String resourceType;

    /**
     * 资源UUID
     */
    @JsonProperty("resource_id")
    private String resourceId;

}
