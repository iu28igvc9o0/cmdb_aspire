package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleDetailParentsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RolePermission;

import lombok.Data;
import lombok.NoArgsConstructor;



/**
*  调用Rbac角色详情响应类型
* Project Name:composite-service
* File Name:RabcRoleDetailResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: RabcRoleDetailResponse <br/>
* date: 2017年8月28日 上午11:07:34 <br/>
*  详细描述这个类的功能等
* @author yangshilei
* @version
* @since JDK 1.6
*/

@NoArgsConstructor
@Data

public class RabcRoleDetailPayload {

	@JsonProperty("parents")
	@JSONField(name = "parents")
	private List<RoleDetailParentsResponse> roleParentDataList;

	private String uuid;

	private String name;

	private String namespace;

	@JsonProperty("describe")
	private String describe;
	

	@JsonProperty("admin_role")
	private Boolean adminRole;
	

    @JsonProperty("role_type")
    private Integer roleType;

	@JsonProperty("permissions")
	@JSONField(name = "permissions")
	private List<RolePermission> rolePemissionList;

	@JsonProperty("created_at")
	@JSONField(name = "created_at")
	private String createTime;

	@JsonProperty("updated_at")
	@JSONField(name = "updated_at")
	private String updateTime;

	@JsonProperty("create_by")
    private String createBy;

}

