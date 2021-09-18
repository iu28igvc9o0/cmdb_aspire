package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class JobConfigPageVo extends JobDTO {
	// 任务配置ID
	@JsonProperty("config_uuid")
	private String configUuid;
	// 任务配置名称
	@JsonProperty("config_name")
	private String configName;
	// job最后一次执行记录（JSON格式，包括上一次执行时间，执行状态）
	@JsonProperty("last_job")
	private JSONObject lastJob;

	// 任务配置的创建时间
	@JsonProperty("created_at")
	private String createdAt;

	// 任务配置的更新时间
	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("resource_actions")
	private List<String> resourceActions;
}
