package com.aspire.mirror.ops.biz.model;

import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class OpsMessageMeta {

	@JsonProperty("step_instance_id")
	private Long	stepInstanceId;

	@JsonProperty("step_id")
	private Long	stepId;

	@JsonProperty("pipeline_instance_id")
	private Long	pipelineInstanceId;

	@JsonProperty("ops_type")
	private Integer	opsType;					// 参考OpsStepDTO中ops类型定义: 0 脚本执行 1 文件分发 2结果文件存储

	@JsonProperty("trigger_way")
	private Integer	triggerWay;					// 消息触发方式
	
	@JsonProperty("extend_meta")
	private OpsMessageExtendMeta extendMeta;	// 扩展meta
}
