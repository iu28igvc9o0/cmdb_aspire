package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* Filter a list of mixed resource types and actions
* Project Name:composite-service
* File Name:AuthActionsBulkRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload
* ClassName: AuthActionsBulkRequest <br/>
* date: 2017年8月27日 下午9:32:14 <br/>
* Filter a list of mixed resource types and actions
* @author pengguihua
* @version
* @since JDK 1.6
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthMixedFilterRequest {
	/**
	 * 成员名称
	 */
    @JsonProperty("username")
	private String username;

	/**
	 * 根帐号
	 */
	@JsonProperty("namespace")
	private String namespace;

	@JsonProperty("is_admin")
	private Boolean isAdmin;

	/**
	 * 资源列表;
	 * 单个资源形式
	 * {
     *  "uuid": "123-321-123",
     *  "name": "my-web-another",
     *  "res:cluster": "dev-cluster",
     *  "res:space": "dev-space",
     *  "action": "application:update"
     * }
	 */
	@JsonProperty("resources")
	private List<RbacResource> resources;

	/**
	 * 资源约束
	 */
	@JsonProperty("context")
	private Map<String, String> context;
}
