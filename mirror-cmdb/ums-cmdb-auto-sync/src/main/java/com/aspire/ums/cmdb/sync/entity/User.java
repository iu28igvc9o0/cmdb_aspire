package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private Office company; // 归属公司
	private Office office; // 归属部门
	@JsonProperty("username")
	private String loginName;// 登录名
	private String password;// 密码
	private String no; // 工号
	private String name; // 姓名
	@JsonProperty("email")
	private String email; // 邮箱
	private String phone; // 电话
	private String mobile; // 手机
	private String userType;// 用户类型
	private String lockFlag;// lockFlag对应表中lock_flag（锁定标识, 0=正常, 1=锁定）
	private String loginIp; // 最后登陆IP
	private Date loginDate; // 最后登陆日期
	private String id;
	private String deptId; // 部门ID
	private Date createDate;
	private String remarks;
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	protected String suspendFlag; //挂起标识, 1:挂起 0:未挂起
	protected String from; //数据来源
	protected String descr; //描述





    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getSuspendFlag() {
		return suspendFlag;
	}

	public void setSuspendFlag(String suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

	public User(String id, Office company, Office office, String loginName, String password, String no, String name,
				String email, String phone, String mobile, String userType, String remarks, Date createDate) {
		super();
		this.id = id;
		this.company = company;
		this.office = office;
		this.loginName = loginName;
		this.password = password;
		this.no = no;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.mobile = mobile;
		this.userType = userType;

		this.createDate = createDate;

	}
	
	
	public User(String id, Office company, Office office, String loginName, String password, String name,
			String email,  String mobile, String userType,String remarks, Date createDate) {
		
	
		super();
		this.id = id;
		this.company = company;
		this.office = office;
		this.loginName = loginName;
		this.password = password;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.userType = userType;
		this.remarks = remarks;
		this.createDate = createDate;

	}
	public User(String id, Office company, Office office, String loginName, String password, String name,
			String email,  String mobile, String userType,String remarks, Date createDate,String departmentCode,String descr,String suspendFlag) {


		super();
		this.id = id;
		this.company = company;
		this.office = office;
		this.loginName = loginName;
		this.password = password;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.userType = userType;
		this.remarks = remarks;
		this.createDate = createDate;
		this.deptId = departmentCode;
		this.descr = descr;
		this.suspendFlag = suspendFlag;
	}
	public User(String loginName) {
		super();
		this.loginName = loginName;
	}
	public User() {
		super();
	}

}