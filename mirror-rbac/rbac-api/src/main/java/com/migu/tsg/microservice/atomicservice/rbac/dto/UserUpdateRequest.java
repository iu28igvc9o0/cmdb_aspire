package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_user修改对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     User
 * 类描述:     monitor_user修改请求参数对象
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
 
@Data
@NoArgsConstructor
public class UserUpdateRequest implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 455105794698334264L;

	/** 主键 */
	@JsonProperty("uuid")
	private String uuid;
	
	/** 姓名 */
	@JsonProperty("name")
	private String name;
	
	/** 用户类型 */
	@JsonProperty("user_type")
	private Integer userType;
	
	/** 所属部门 */
	@JsonProperty("dept_id")
	private String deptId;
	
	/** 人员编号 */
	@JsonProperty("no")
	private String no;
	
	/** 性别 */
	@JsonProperty("sex")
	private Integer sex;
	
	/** 邮箱 */
	@JsonProperty("mail")
	private String mail;
	
	/** 地址 */
	@JsonProperty("address")
	private String address;
	
	/** 电话号码 */
	@JsonProperty("phone")
	private String phone;
	
	/** 移动号码 */
	@JsonProperty("mobile")
	private String mobile;
	
	/** 账号id */
	@JsonProperty("ldapId")
	private String ldapId;
	
	/** 空间 */
	@JsonProperty("namespace")
	private String namespace;
	/** 人员代码 */
	 @JsonProperty("code")
	private String code;
	/** 传真 */
	 @JsonProperty("fax")
	private String fax;
		/** 职责 */
	 @JsonProperty("post")
	private String post;
		/** 关联人 */
	 @JsonProperty("relation_person")
	private String relationPerson;
	/** 描述 */
	 @JsonProperty("descr")
	private String descr;

	/**
	 * 头像
	 */
	@JsonProperty("picture")
	private String picture;

	@JsonProperty("dept_ids")
	private List<String> deptIds;
}
