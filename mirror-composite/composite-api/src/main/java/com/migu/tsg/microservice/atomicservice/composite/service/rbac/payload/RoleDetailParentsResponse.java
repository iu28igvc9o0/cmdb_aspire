package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* RoleDetailParentsResponse类
* Project Name:composite-api
* File Name:RoleDetailParentsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RoleDetailParentsResponse <br/>
* date: 2017年9月1日 下午12:36:43 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RoleDetailParentsResponse {
	private String uuid;
	private String name;
	@JsonProperty("assigned_at")
	private String assignedTime;
	@JsonProperty("resource_actions")
	private List<String> roleAction;
}
