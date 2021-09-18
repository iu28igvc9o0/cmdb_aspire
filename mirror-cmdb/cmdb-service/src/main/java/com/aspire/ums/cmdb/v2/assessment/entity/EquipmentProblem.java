package com.aspire.ums.cmdb.v2.assessment.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 设备问题
 */
@Data
@NoArgsConstructor
public class EquipmentProblem { 

	// id
	private String id;

	// 问题名称
	private String problemName;

	// 问题级别
	private String problemLevel;

   // 省份
	private String province;

	// 发生地市
	private String city;

	// 厂家
	private String produce;

	// 设备类型
	private String deviceType;

	// 设备名称
	private String deviceName;

	// 典配类型
	private String configType;

	// 发生时间
	private Date beginDate;

	// 状态
	private String status;

	// 解决时间
	private Date solveDate;

	// 问题/需求
	private String problemDemand;

	// 问题提出人
	private String problemPerson;

	// 填报人
	private String person;

	// 电话
	private String phone;

	// 问题详情
	private String detail;

	// 审批状态
	private String assessStatus;

	// 填报时间
	private Date createTime;

	// 更新时间
	private Date updateTime;

	// 所属季度
	private String quarter;
      
}
