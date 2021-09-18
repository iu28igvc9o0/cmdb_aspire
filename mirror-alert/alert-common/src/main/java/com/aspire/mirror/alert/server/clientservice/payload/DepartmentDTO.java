package com.aspire.mirror.alert.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.dto.model   
 * 类名称:     Department
 * 类描述:    业务类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DepartmentDTO implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -3662189025781533500L;

	/** id */
	 @ApiModelProperty(value = "id")
	 @JsonProperty("uuid")
	private String uuid;
	
	/** 父节点id */
	 @ApiModelProperty(value = "父节点id")
	 @JsonProperty("parent_id")
	private String parentId;
	
	/** 部门名称 */
	 @ApiModelProperty(value = "部门名称")
	 @JsonProperty("name")
	private String name;
	
	/** 部门编号 */
	 @ApiModelProperty(value = "部门编号")
	 @JsonProperty("no")
	private String no;
	
	/** 部门类型 */
	 @ApiModelProperty(value = "部门类型")
	 @JsonProperty("dept_type")
	private Integer deptType;
	
	/** 部门描述 */
	 @ApiModelProperty(value = "部门描述")
	 @JsonProperty("descr")
	private String descr;
	
	/** 部门层级 */
	 @ApiModelProperty(value = "部门层级")
	 @JsonProperty("level")
	private Integer level;
	
	/** 空间 */
	 @ApiModelProperty(value = "空间")
	 @JsonProperty("namespace")
	private String namespace;	 

	private boolean top=false;
	
}
