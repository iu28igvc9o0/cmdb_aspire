package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* CompAuthBulkActionsRequest类
* Project Name:composite-service
* File Name:AuthActionsRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientService.rabc.payload
* ClassName: CompAuthBulkActionsRequest <br/>
* date: 2017年9月1日 下午1:54:15 <br/>
* 批量actions获取请求对象
* @author yangshilei
* @version
* @since JDK 1.6
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompAuthBulkActionsRequest {
	/**
	 * 成员名称
	 */
	private String username;

	/**
	 * 空间名称/根账号
	 */
	private String namespace;

	/**
    * 是否管理员帐号
    * @since JDK 1.6
    */
	@JsonProperty("is_admin")
    private Boolean isAdmin;

    /**
     * 资源名称
     */
	@JsonProperty("resource_type")
    private String resourceType;

	/**
	 * 资源属性
	 */
	@JsonProperty("resources")
	@JSONField(name = "resources")
	private List<RbacResource> rabcResourceList;

    /**
    * 上下文约束
    * @since JDK 1.6
    */
	@JsonProperty("context")
    private Map<String, String> context;
}