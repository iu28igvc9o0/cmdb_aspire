package com.aspire.mirror.ops.api.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsPipelineInstance
 * <p/>
 *
 * 类功能描述: 作业流水实例
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
@EqualsAndHashCode(of= {"pipelineInstanceId"})
@ToString(of={"pipelineInstanceId", "pipelineId", "pipelineInstanceName", "status", "currStepId"})
@JsonInclude(Include.NON_NULL)
public class OpsPipelineInstanceDTO {
	public static final String[] NO_AUTH_LABEL				= {"autoRepair","cruisecheck"};
	public static final long	NO_PIPELINE_ID				= -1L;
	public static final int		CLASSIFY_REALTIME_SCRIPT	= 1;
	public static final int		CLASSIFY_FILE_TRANSFER		= 2;
	public static final int		CLASSIFY_PIPELINE			= 9;
	public static final Integer	BIZ_CLASSIFY_OPS			= 0;	// 通用OPS操作, 包括      脚本执行、文件分发、作业
	public static final Integer	BIZ_CLASSIFY_INDEX_COLLECT	= 1;	// 脚本指标采集
	public static final Integer	BIZ_CLASSIFY_AUTO_REPAIR	= 5;	// 自愈作业执行
	public static final Integer	BIZ_CLASSIFY_VUL_REPAIR		= 6;	// 漏洞修复作业执行
	public static final Integer	BIZ_CLASSIFY_VUL_GO_BACK	= 7;	// 漏洞回退作业执行
	public static final Integer	BIZ_CLASSIFY_VUL_RECHECK	= 8;	// 漏洞复查作业执行
	@JsonProperty("pipeline_instance_id")
	protected Long				pipelineInstanceId;
	@JsonProperty("pipeline_id")
	protected Long				pipelineId;
	@JsonProperty("pipeline_instance_name")
	protected String			pipelineInstanceName;
	@JsonProperty("operator")
	protected String			operator;										// 操作发起人
	@JsonProperty("trigger_way")
	protected Integer			triggerWay					= OpsTriggerWayEnum.TRIGGER_BY_MANUAL.getStatusCode();
	@JsonProperty("curr_step_id")
	protected Long				currStepId;
	@JsonProperty("status")
	protected Integer			status;

	@JsonProperty("aspnode_result")
	private Integer	aspNodeResult;		// 从脚本执行结果中提取出来的  aspnode_result 标记位         0: 成功     1: 失败

	@JsonProperty("output_path")
	private String outputPath;

	// 作业当前状态
	@JsonProperty("start_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				startTime;
	
	@JsonProperty("end_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				endTime;
	
	@JsonProperty("total_time")
	protected Float				totalTime;
	
	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				createTime;
	
	@JsonProperty("instance_classify")
	protected Integer			instanceClassify;									// 分类： 1.实时脚本作业   2.实时文件分发   9. 流程作业
	
	@JsonProperty("biz_classify")
	protected Integer			bizClassify = BIZ_CLASSIFY_OPS;						// 业务分类:  0 通用ops操作      1 监控项脚本采集
	
	@JsonProperty("biz_meta")
	protected final Map<String, Object>	bizMeta	= new HashMap<String, Object>();	// 业务扩展meta
	
	@JsonProperty("label_id")
	protected String			labelId;
	
	@JsonProperty("label_name")
	protected String			labelName;

	@JsonProperty("review_apply_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				reviewApplyTime;

	@JsonProperty("review_applicant")
	protected String				reviewApplicant;
	
	/** 
	 * 功能描述: 返回 整个pipelineInstance是否运行完成
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public boolean isExecuteOver() {
		if (status == null) {
			return false;
		}
		OpsStatusEnum statusEnum = OpsStatusEnum.of(status);
		if (statusEnum == OpsStatusEnum.STATUS_101 
				|| statusEnum == OpsStatusEnum.STATUS_102 
				|| statusEnum == OpsStatusEnum.STATUS_9) {
			return true;
		}
		return false;
	}
	
	/** 
	 * 功能描述: 添加业务扩展属性  
	 * <p>
	 * @param atrrKey
	 * @param atrrVal
	 */
	public void putBizMetaAttr(String atrrKey, Object atrrVal) {
		bizMeta.put(atrrKey, atrrVal);
	}
	
	public void putAllBizMetaAttr(Map<String, Object> bizMetaObj) {
		bizMeta.putAll(bizMetaObj);
	}
}
