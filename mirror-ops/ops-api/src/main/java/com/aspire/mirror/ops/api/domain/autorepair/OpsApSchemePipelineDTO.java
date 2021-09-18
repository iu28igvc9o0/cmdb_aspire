/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsApSchemePipelineDTO.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月6日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain.autorepair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.Getter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApSchemePipelineDTO
 * <p/>
 *
 * 类功能描述: 自愈方案引用的作业
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OpsApSchemePipelineDTO {
	public static final String	JUDGE_TYPE_EXEC_STATUS		= "exec_status";
	public static final String	JUDGE_TYPE_ASPNODE_RESULT	= "aspnode_result";
	public static final String	JUDGE_TYPE_ASPNODE_MSG		= "aspnode_msg";

	public static final int		FINISH_ACTION_OVER			= 0;	// 自动终止
	public static final int		FINISH_ACTION_CONTINUE		= 1;	// 继续执行
	public static final int		FINISH_ACTION_MANUAL		= 2;	// 人工干预
	
	protected Long		id;
	protected Long		schemeId;
	protected Long		pipelineId;
	protected String	pipelineName;
	protected String	finishJudgeType;		// exec_status|aspnode_result|aspnode_msg
	protected String	finishJudgeValue;		// 判断值
	protected Integer	finishJudgeAction;		// 判断动作	0: 自动终止  1：继续执行  2： 人工干预
	protected Integer	order;				
	
	
	/** 
	 * 作业值判断类型枚举
	 */
	@Getter
	public static enum SchemePipeJudgeTypeEnum {
		EXEC_STATUS("exec_status", "完成状态"), ASPNODE_MSG("aspnode_msg", "aspnode_msg"), 
		ASPNODE_RESULT("aspnode_result", "aspnode_result");

		private SchemePipeJudgeTypeEnum(String code, String label) {
			this.code = code;
			this.label = label;
		}

		private String	code;
		private String	label;
	}
	
	/** 
	 * 作业执行状态枚举
	 */
	@Getter
	public static enum SchemePipeExecStatusEnum {
		STATUS_SUC("success", "执行成功"), STATUS_FAIL("fail", "执行失败");

		private SchemePipeExecStatusEnum(String code, String label) {
			this.code = code;
			this.label = label;
		}

		private String	code;
		private String	label;
	}
	
	
	/** 
	 * 作业执行完成后动作枚举
	 * 0: 自动终止  1：继续执行  2： 人工干预
	 */
	@Getter
	public static enum SchemePipeFinishActionEnum {
		OVER(0, "自动终止"), CONTINUE(1, "继续执行"), STATUS_FAIL(2, "人工干预");

		private SchemePipeFinishActionEnum(Integer code, String label) {
			this.code = code;
			this.label = label;
		}

		private Integer	code;
		private String	label;
	}
}
