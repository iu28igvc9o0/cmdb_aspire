package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
*  角色详情响应
* Project Name:composite-service
* File Name:RoleDetailResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: RoleDetailResponse <br/>
* date: 2017年8月28日 上午11:07:09 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RoleDetailResponse {
    //测试获取角色详情
    @JsonProperty("namespace")
    private String orgAccount;
    
	private String uuid;

	@JsonProperty("parents")
	private List<RoleDetailParentsResponse> roleParentDataList;

	@JsonProperty("admin_role")
	private Boolean adminRole;

    @JsonProperty("role_type")
    private Integer roleType;

	@JsonProperty("name")
	private String name;
    
    @JsonProperty("describe")
    private String describe;

	@JsonProperty("permissions")
	private List<RolePermission> rolePemissionList;

	@JsonProperty("created_at")
	private String createTime;

	@JsonProperty("updated_at")
	private String updateTime;

	@JsonProperty("resource_actions")
	private List<String> resourceAction;

	@JsonProperty("created_by")
	private String createBy;
}
