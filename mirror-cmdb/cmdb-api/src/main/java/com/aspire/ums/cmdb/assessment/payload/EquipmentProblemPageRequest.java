package com.aspire.ums.cmdb.assessment.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

 
@NoArgsConstructor
@Data
public class EquipmentProblemPageRequest { 

 
   // 分页页标
   @JsonProperty("page_size")
   private String pageSize;

   // 每页数量
   @JsonProperty("page_no")
   private String pageNo;

	// 问题名称
	@JsonProperty("problem_name")
	private String problemName;

	// 问题级别
	@JsonProperty("problem_level")
	private String problemLevel;

   // 省份
	private String province;

	// 发生地市
	private String city;

	// 厂家
	@JsonProperty("produce")
	private String produce;

	// 设备类型
   // @JsonProperty("device_type")
	private String deviceType;

	// 设备名称
	@JsonProperty("device_name")
	private String deviceName;

	// 典配类型
	@JsonProperty("config_type")
	private String configType;

	// 发生时间
	@JsonProperty("begin_date")
	private Date beginDate;

	// 状态
	private String status;

	// 解决时间
	@JsonProperty("solve_date")
	private Date solveDate;


	// 问题/需求
   @JsonProperty("problem_demand")
	private String problemDemand;

	// 问题提出人
	private String person;

	// 电话
	private String phone;

	// 问题详情
	private String detail;

   // 审批状态
   @JsonProperty("assess_status")
	private String assessStatus;

	// 更新时间
	@JsonProperty("update_time")
	private Date updateTime;

	// 创建时间
	@JsonProperty("create_time")
	private Date createTime;

	/**
	 * 所属季度
	 */
	private String quarter;

   	   
}
