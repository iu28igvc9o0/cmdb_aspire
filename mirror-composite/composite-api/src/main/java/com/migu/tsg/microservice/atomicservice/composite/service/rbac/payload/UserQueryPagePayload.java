package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UserQueryPagePayload implements Serializable{
	/** 
	 * @fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -6150877489125153797L;

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

	 private String search;
	
}
