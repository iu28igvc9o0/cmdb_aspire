package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.dto.model   
 * 类名称:     User
 * 类描述:    业务类，定义与表字段对应的属性
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class UserDTO implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -5106364652078746810L;

	/** 主键 */
	 @ApiModelProperty(value = "主键")
	 @JsonProperty("uuid")
	private String uuid;
	
	/** 姓名 */
	 @ApiModelProperty(value = "姓名")
	 @JsonProperty("name")
	private String name;
	
	/** 用户类型 */
	 @ApiModelProperty(value = "用户类型")
	 @JsonProperty("user_type")
	private Integer userType;
	
	/** 所属部门 */
	 @ApiModelProperty(value = "所属部门")
	 @JsonProperty("dept_id")
	private String deptId;
	
	/** 人员编号 */
	 @ApiModelProperty(value = "人员编号")
	 @JsonProperty("no")
	private String no;
	
	/** 性别 */
	 @ApiModelProperty(value = "性别")
	 @JsonProperty("sex")
	private Integer sex;
	
	/** 邮箱 */
	 @ApiModelProperty(value = "邮箱")
	 @JsonProperty("mail")
	private String mail;
	
	/** 地址 */
	 @ApiModelProperty(value = "地址")
	 @JsonProperty("address")
	private String address;
	
	/** 电话号码 */
	 @ApiModelProperty(value = "电话号码")
	 @JsonProperty("phone")
	private String phone;
	
	/** 移动号码 */
	 @ApiModelProperty(value = "移动号码")
	 @JsonProperty("mobile")
	private String mobile;
	
	/** 账号id */
	 @ApiModelProperty(value = "账号id")
	 @JsonProperty("ldapId")
	private String ldapId;
	
	/** 空间 */
	 @ApiModelProperty(value = "空间")
	 @JsonProperty("namespace")
	private String namespace;
	 

	/** 人员代码 */
	 @ApiModelProperty(value = "人员代码")
	 @JsonProperty("code")
	private String code;
	/** 传真 */
	 @ApiModelProperty(value = "传真")
	 @JsonProperty("fax")
	private String fax;
		/** 职责 */
	 @ApiModelProperty(value = "职责")
	 @JsonProperty("post")
	private String post;
		/** 关联人 */
	 @ApiModelProperty(value = "关联人")
	 @JsonProperty("relation_person")
	private String relationPerson;
		/** 描述 */
	 @ApiModelProperty(value = "描述")
	 @JsonProperty("descr")
	private String descr;


	/**
	 * 头像
	 */
	@JsonProperty("picture")
	@ApiModelProperty(value = "头像")
	private String picture;
	

	@JsonProperty("excel")
	private boolean excel=false;

	/**
	 * 密码更新时间
	 */
	@JsonProperty("ldap_password_updatetime")
	private Timestamp ldapPasswordUpdatetime;

	/**
	 * 账号状态
	 */
	@JsonProperty("ldap_status")
	private String ldapStatus;

	@JsonProperty("roles")
	private String roles;

	@JsonProperty("dept_ids")
	private List<String> deptIds;

	@JsonProperty("dept_list")
	private List<DepartmentDTO> deptList;
}
