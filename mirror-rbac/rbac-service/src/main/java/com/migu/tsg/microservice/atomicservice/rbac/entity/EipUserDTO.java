package com.migu.tsg.microservice.atomicservice.rbac.entity;

import lombok.Data;

@Data
public class EipUserDTO {
//	"userLogin": "limengmeng",账号
//    "userName": "李萌萌",姓名
//    "userCode": "004775",工号
//    "tel": "13910817745",电话
//    "mail": "limengmeng@aspirecn.com",邮箱
//    "dept": "卓望公司/卓望信息/财务部",部门名称
//    "userType": "正式员工",员工性质
//    "sort": "80058006199ZZ004775",排序
//    "timestamp": "0000000008761515",时间戳
//    "staff": "税务管理主管",职称
//    "deptId": "D00005",部门ID
	
	private String userLogin;
	private String userName;
	private String userCode;
	private String tel;
	private String mail;
	private String dept;
	private String userType;
	private String sort;
	private String timestamp;
	private String staff;
	private String deptId;
}
