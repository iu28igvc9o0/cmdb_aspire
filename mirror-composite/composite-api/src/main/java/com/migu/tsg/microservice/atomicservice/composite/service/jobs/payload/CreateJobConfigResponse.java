package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateJobConfigResponse extends JobDTO {

	// job最后一次执行记录（JSON格式，包括上一次执行时间，执行状态）
	@JsonProperty("last_job")
	private String lastJob;

	// 定时配置ID
	@JsonProperty("schedule_config_id")
	private String scheduleConfigId;

	// 定时配置的私钥
	@JsonProperty("schedule_config_secret_key")
	private String scheduleConfigSecretKey;

	// 环境变量
	private String envvars;

	// 环境变量文件
	private String envfiles;

	// 任务配置的创建时间
	@JsonProperty("created_at")
	private String createdAt;

	// 任务配置的更新时间
	@JsonProperty("updated_at")
	private String updatedAt;

	// 任务配置ID
	@JsonProperty("config_uuid")
	private String configUuid;
	// 任务配置名称
	@JsonProperty("config_name")
	private String configName;
}
