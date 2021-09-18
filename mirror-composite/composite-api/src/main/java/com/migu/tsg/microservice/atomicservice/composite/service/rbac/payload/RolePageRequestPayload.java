package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_role新增对象类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     Role
 * 类描述:     monitor_actions分页查询对象
 * 创建人:     曾祥华
 * 创建时间:     2019-03-30 14:42:39
 */
 
@Data
@NoArgsConstructor
public class RolePageRequestPayload{

    /**
    * 每页显示记录条数
    */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
    * 第几页
    */
    @JsonProperty("page_no")
    private Integer pageNo;

	/** 角色UUID主键 */
	@JsonProperty("uuid")
	private String uuid;
	
	/** 角色名称 */
	@JsonProperty("name")
	private String name;
	
	/** 角色类型 */
	@JsonProperty("role_type")
	private Integer roleType;
	
	/** 空间名 */
	@JsonProperty("namespace")
	private String namespace;
	
	/** 是否为管理员角色 */
	@JsonProperty("admin_role")
	private Integer adminRole;
	
	/** 角色描述 */
	@JsonProperty("descr")
	private String descr;
	
	/** 创建时间 */
	@JsonProperty("created_at")
	private Date createdAt;
	
	/** 更新时间 */
	@JsonProperty("updated_at")
	private Date updatedAt;
	
	
}
