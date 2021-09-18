package com.aspire.mirror.alert.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


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
@NoArgsConstructor
public class AlertUserVO implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -6150877489125153797L;

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

	@JsonProperty("dept_list")
	private List<DepartmentDTO> deptList;
}
