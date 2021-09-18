package com.aspire.ums.cmdb.assessment.payload;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

 
@NoArgsConstructor
@Data
public class EquipmentProblemRequest { 

 
   // id
	private String id;

	// 问题名称
	@JSONField(name="problem_name")
	private String problemName;

	// 问题级别
	@JSONField(name="problem_level")
	private String problemLevel;

   // 省份
	private String province;

	// 发生地市
	private String city;

	// 厂家
	@JSONField(name="produce")
	private String produce;

	// 设备类型
	//@JsonProperty("device_type")
	private String deviceType;

	// 设备名称
	@JSONField(name="device_name")
	private String deviceName;

	// 典配类型
	@JSONField(name="config_type")
	private String configType;

	// 发生时间
	@JSONField(name="begin_date")
	private Date beginDate;

	// 状态
	private String status;

	// 解决时间
	@JSONField(name="solve_date")
	private Date solveDate;


	// 问题/需求
   @JSONField(name="problem_demand")
	private String problemDemand;

	// 问题提出人
	@JSONField(name="problem_person")
	private String problemPerson;

	// 问题提出人
	private String person;

	// 电话
	private String phone;

	// 问题详情
	private String detail;

   // 审批状态
   @JSONField(name="assess_status")
	private String assessStatus;

	// 更新时间
	@JSONField(name="update_time")
	private Date updateTime;

	// 创建时间
	@JSONField(name="create_time")
	private Date createTime;

	/**
	 * 所属季度
	 */
	private String quarter;


}
