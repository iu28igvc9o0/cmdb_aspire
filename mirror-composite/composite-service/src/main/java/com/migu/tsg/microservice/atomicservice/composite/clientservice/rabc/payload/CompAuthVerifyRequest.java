package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* Verify接口请求对象
* Project Name:composite-service
* File Name:CompAuthVerifyRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload
* ClassName: AuthVerifyRequest <br/>
* date: 2017年8月27日 下午6:31:20 <br/>
* Verify接口请求对象
* @author pengguihua
* @version
* @since JDK 1.6
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompAuthVerifyRequest {
	/**
	 * 根账号/成员名称
	 */
    @JsonProperty("username")
	private String username;

	/**
	 * 空间名称/根账号
	 */
	@JsonProperty("namespace")
	private String namespace;

	/**
	 * 资源名称
	 */
	@JsonProperty("is_admin")
	private boolean isAdmin;

	/**
	* 资源属性  如    "resource": { "name: "my-service"　}
	* @since JDK 1.6
	*/
	@JsonProperty("resource")
	private RbacResource resource;

	/**
	 * 资源操作
	 */
	@JsonProperty("action")
	@JSONField(name = "action")
	private String resTypeAction;      // 带资源前缀的action 如     service:view

	/**
	 * 资源约束 如：  "context": {"res:cluster": "dev"}
	 */
	@JsonProperty("context")
	private Map<String, String> context;
}
