/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsPipelineRunJob.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年12月13日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsPipelineRunJob
 * <p/>
 *
 * 类功能描述: 定时作业
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年12月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class OpsPipelineRunJob {
	@JsonProperty("job_id")
	private Long	jobId;

	@JsonProperty("name")
	private String	name;

	@JsonProperty("pipeline_id")
	private Long	pipelineId;
	
	@JsonProperty("pipeline_name")
	private String	pipelineName;

	@JsonProperty("cron_expression")
	private String	cronExpression;

	@JsonProperty("status")
	private Integer	status = OpsStatusEnum.STATUS_6.getStatusCode();

	@JsonProperty("creater")
	private String	creater;

	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@JsonProperty("updater")
	private String	updater;

	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	updateTime;
	
	@Data
	public static class OpsPipelineRunJobQueryModel {
		private String	name;
		private String	nameLike;
		private String	createrLike;
		private String	updaterLike;

		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTimeStart;
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTimeEnd;

		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	updateTimeStart;
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	updateTimeEnd;

		@JsonProperty("page_size")
		private Integer	pageSize;		// 如果为null， 则查询全部数据
		@JsonProperty("page_no")
		private Integer	pageNo;			// 从0开始

		public Integer getStartIdx() {
			if (pageSize == null) {
				return null;
			}
			return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
		}
	}
}
