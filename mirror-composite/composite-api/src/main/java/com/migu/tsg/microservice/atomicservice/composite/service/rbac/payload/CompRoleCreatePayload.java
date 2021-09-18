package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 新增Role时请求和响应payload
* Project Name:composite-service
* File Name:CompRoleCreateRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.payload
* ClassName: CompRoleCreateRequest <br/>
* date: 2017年8月28日 下午12:29:04 <br/>
* 新增Role时请求和响应payload
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class CompRoleCreatePayload {
    // 只包含在响应消息中
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("describe")
    private String describe;

    @JsonProperty("resources")
    private List<String> resources;

    @JsonProperty("parents")
    private List<RoleCreateParent> roleParentList;
    //测试发现前期的RolePermission有多余属性，重新创建RolePermissionRequest替换
    @JsonProperty("permissions")
    private List<RolePermissionRequest> rolePermissions;

    //角色更新使用
    @JsonProperty("admin_role")
    private Boolean adminRole = false;

    //角色更新使用
    @JsonProperty("created_at")
    private String createTime;
    //角色更新使用
    @JsonProperty("updated_at")
    private String updateTime;

    /**
    * RoleCreateParent类
    * Project Name:composite-api
    * File Name:CompRoleCreatePayload.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
    * ClassName: RoleCreateParent <br/>
    * date: 2017年9月1日 上午11:35:58 <br/>
    * @author yangshilei
    * @version CompRoleCreatePayload
    * @since JDK 1.6
    */
    @NoArgsConstructor
    @Data
    private static class RoleCreateParent {
        private String uuid;
        private String name;
        @JsonProperty("assigned_at")
        private String assignedAt;
    }
}
