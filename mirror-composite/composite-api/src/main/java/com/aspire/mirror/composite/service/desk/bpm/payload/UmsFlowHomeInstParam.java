package com.aspire.mirror.composite.service.desk.bpm.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UmsFlowHomeInstParam {
	/**
	 * 选中Tab类型 1待办 2已办 3我发起
	 */
	private Integer tab;

	/**
	 * 状态 running处理中  end关闭 传空表示全部
	 */
    private String status;
    
	@NotNull
	private Integer pageNo;
	
	@NotNull
	private Integer pageSize;
	
	/**
	 *  选中代办预警,勾选传Y ，没勾选传空
	 */
	private String warnFlag;
	/**
	 *  选中代办超时，勾选传Y ，没勾选传空
	 */
	private String timeOutFlag;
	
	/**
	 * 工单类型: 1-全部工单;2-我的工单
	 */
	private Integer instType;
	
	/**
	 * 时间类型： 1-上周；2-上月
	 */
	private Integer dateType;
	
	/**
	 * 选中工单： 1. 工单总数 2处理中工单 3超时工单 4关闭工单
	 */
	private Integer workType;
	
	/**
	 * 选了某个服务业务部下面组织
	 */
	private String orgName;
	
	/**
	 * 工单查询参数。
	 */
	private String instQueryParam;
	
	/**
	 * 告警类型 1：资源池，2:项目名称
	 */
	private int alarmType;
	
}
