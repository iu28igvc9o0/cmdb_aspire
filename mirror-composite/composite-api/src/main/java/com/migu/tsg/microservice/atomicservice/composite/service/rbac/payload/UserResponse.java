package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.afterturn.easypoi.excel.annotation.Excel;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.dto.vo   
 * 类名称:     User
 * 类描述:     数据模型
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
 
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -6150877489125153797L;
	
	/** 主键 */
	@JsonProperty("uuid")
	private String uuid;
	
	/** 姓名 */
	 @JsonProperty("name")
	 @Excel(name = "人员名称")
	private String name;
	
	/** 用户类型 */
	 @JsonProperty("user_type")
	 @Excel(name = "人员类型")
	private Integer userType;
	
	/** 所属部门 */
	 @JsonProperty("dept_id")
	 @Excel(name = "所属部门")
	private String deptId;
	
	/** 人员编号 */
	 @JsonProperty("no")
	 @Excel(name = "人员编号")
	private String no;
	
	/** 性别 */
	 @JsonProperty("sex")
	 @Excel(name = "性别")
	private Integer sex;
	
	/** 邮箱 */
	 @JsonProperty("mail")
	 @Excel(name = "邮箱")
	private String mail;
	
	/** 地址 */
	 @JsonProperty("address")
	 @Excel(name = "地址")
	private String address;
	
	/** 电话号码 */
	 @JsonProperty("phone")
	 @Excel(name = "电话号码")
	private String phone;
	
	/** 移动号码 */
	 @JsonProperty("mobile")
	 @Excel(name = "移动电话")
	private String mobile;
	
	/** 账号id */
	 @JsonProperty("ldapId")
	private String ldapId;
	 

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
	 * 密码更新时间
	 */
	@JsonProperty("ldap_password_updatetime")
	private Timestamp ldapPasswordUpdatetime;

	/**
	 * 账号状态
	 */
	@JsonProperty("ldap_status")
	private Integer ldapStatus;

	@JsonProperty("dept_list")
	private List<DepartmentResponse> deptList;
}
