package com.migu.tsg.microservice.atomicservice.composite.vo.rbac;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* RABC鉴权返回的 Resource对象
* Project Name:composite-service
* File Name:RbacResource.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: RbacResource <br/>
* date: 2017年8月27日 下午9:23:15 <br/>
* RABC鉴权返回的 Resource对象
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@ToString
@JsonInclude(Include.NON_NULL)
public class RbacResource {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("action")
    private String resTypeAction;   // 资源操作,包含资源类型前缀, 如  service:view

    @JsonProperty("res:cluster")
    private String consCluster;     // 资源 cluster约束

    @JsonProperty("res:space")
    private String consSpace;       // 资源 space约束

    @JsonProperty("res:project")
    private String consProject;     // 资源 project约束
    
    @JsonProperty("res:knamespace")
    private String consKnamespace;  // 资源 kNamespace约束

    @JsonProperty("res:subaccount")
    private String subAccount;      // 角色assigned帐号
    
    @JsonProperty("res:application")
    private String consAppName;     // application_name约束
    
    @JsonProperty("unique_name")
    private String appUniqueName;  //标志：确保application 和 service 一一对应
	
	@JsonProperty("res:registry")
    private String registryName;     // registry_name约束
	
	@JsonProperty("res:area")
	private String area;

	@JsonProperty("res:deviceType")
	private String deviceType;

	@JsonProperty("res:device")
	private String device;
	
}
