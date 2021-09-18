package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JobConfigUpdateRequest extends JobDTO {

	// 任务配置ID
	@JsonProperty("config_uuid")
	private String configUuid;
	// 任务配置名称
	@JsonProperty("config_name")
	private String configName;
	// 定时配置ID
	@JsonProperty("schedule_config_id")
	private String scheduleConfigId;

	// 定时配置的私钥
	@JsonProperty("schedule_config_secret_key")
	private String scheduleConfigSecretKey;

	// 环境变量
	private List<EnvvarsDTOCopy> envvars;

	// 环境变量文件
	private List<EnvfilesDTOCopy> envfiles;

	// 任务配置的创建时间
	@JsonProperty("created_at")
	private String createdAt;

	// 任务配置的更新时间
	@JsonProperty("updated_at")
	private String updatedAt;
}
