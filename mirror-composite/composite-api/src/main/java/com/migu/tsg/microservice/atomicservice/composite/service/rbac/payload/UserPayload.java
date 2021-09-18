package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.afterturn.easypoi.excel.annotation.Excel;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

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
public class UserPayload implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -6150877489125153797L;


	/** 人员代码 */
	 @JsonProperty("code")
	 @NotBlank
	 @Excel(name = "用户名称")
	private String code;
	 
	/** 姓名 */
	 @JsonProperty("name")
	 @Excel(name = "真实姓名")
	private String name;
	
	/** 用户类型 */
	 @JsonProperty("user_type")
	 @Excel(name = "用户类型", replace = {"临时用户_2", "正式用户_1"})
	private Integer userType;
	
	/** 所属部门 */
	 @JsonProperty("dept_id")
	 @Excel(name = "部门")
	 private String deptId;
	
	/** 人员编号 */
	 @JsonProperty("no")
	 @Excel(name = "用户编号")
	private String no;
	
	/** 性别 */
	 @JsonProperty("sex")
	 @Excel(name = "性别", replace = {"女_0", "男_1", "-_null"})
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
	 @NotBlank
	private String mobile;
	
	/** 账号id */
	 @JsonProperty("ldapId")
	private String ldapId;
	 
	/** 传真 */
	 @JsonProperty("fax")
	 @Excel(name = "传真")
	private String fax;
		/** 职责 */
	 @JsonProperty("post")
	 @Excel(name = "职责")
	private String post;
		/** 关联人 */
	 @JsonProperty("relation_person")
	 @Excel(name = "关联人")
	private String relationPerson;
		/** 描述 */
	@JsonProperty("descr")
	@Excel(name = "描述")
	private String descr;

	@JsonProperty("deptIds")
	private String deptIds;
}
