/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsPipeline.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsPipelineDTO
 * <p/>
 *
 * 类功能描述: 作业流水
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@Setter
@EqualsAndHashCode(of= {"pipelineName"})
@ToString(of={"pipelineName"})
@JsonInclude(Include.NON_NULL)
public class OpsPipelineDTO extends OpsGroupObject{
	@JsonProperty("pipeline_id")
	protected Long				pipelineId;

	@JsonProperty("pipeline_name")
	protected String			pipelineName;

	@JsonProperty("creater")
	protected String			creater;

	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				createTime;

	@JsonProperty("updater")
	protected String			updater;

	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				updateTime;

	@JsonProperty("label_id")
	protected String			labelId;

	@JsonProperty("label_name")
	protected String			labelName;


	@JsonProperty("step_count")
	protected Integer			stepCount;
	
//	@ApiModelProperty(hidden=true)
	@JsonProperty("remove_step_list")
	protected List<Long>		removeOpsStepIdList;

	@JsonProperty("ops_step_list")
	protected List<OpsStepDTO>	opsStepList;

	@JsonProperty("audit_status")
	private String auditStatus;

	@JsonProperty("audit_desc")
	private String auditDesc;

	@JsonProperty("reviewer")
	private String reviewer;

	@JsonProperty("review_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reviewTime;

	@JsonProperty("current_version")
	private String currentVersion;

	@Data
	public static class OpsPipelineQueryModel extends GeneralAuthConstraintsAware {
		
		@JsonProperty("pipeline_id")
		protected Long	    pipelineId;
		
		@JsonProperty("pipeline_name")
		protected String	pipelineName;

		@JsonProperty("pipeline_name_like")
		private String		pipelineNameLike;

		@JsonProperty("creater_like")
		protected String	createrLike;

		protected String	creater;

		@JsonProperty("updater_like")
		protected String	updaterLike;

		protected String	updater;

		@JsonProperty("update_time_start")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date		updateTimeStart;

		@JsonProperty("update_time_end")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date		updateTimeEnd;

		@JsonProperty("create_time_start")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date		createTimeStart;

		@JsonProperty("create_time_end")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date		createTimeEnd;

		@JsonProperty("labelId")
		protected String	labelId;

		@JsonProperty("page_size")
		private Integer		pageSize;			// 如果为null， 则查询全部数据

		@JsonProperty("page_no")
		private Integer		pageNo;				// 从0开始

		@JsonProperty("audit_status")
		private String auditStatus;

		public Integer getStartIdx() {
			if (pageSize == null) {
				return null;
			}
			return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
		}
	}
}
