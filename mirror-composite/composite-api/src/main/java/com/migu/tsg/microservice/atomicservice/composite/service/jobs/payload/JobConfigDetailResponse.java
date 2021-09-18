package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
//@AllArgsConstructor
public class JobConfigDetailResponse extends JobDTO {

	// 任务配置ID
	@JsonProperty("config_uuid")
	private String configUuid;
	// 任务配置名称
	@JsonProperty("config_name")
	private String configName;

	// job最后一次执行记录（JSON格式，包括上一次执行时间，执行状态）
	@JsonProperty("last_job")
	private LastJob lastJob;

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
	private Date createdAt;

	// 任务配置的更新时间
	@JsonProperty("updated_at")
	private Date updatedAt;

	@JsonProperty("resource_actions")
	private List<String> resourceActions;
	
	// 定时配置ID
	@JsonProperty("region_uuid")
	private String regionUuid;
	
	public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return (Date) this.createdAt.clone();
    }
    public void setCreatedAt(final Date createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Date) createdAt.clone();
        }
    }
    public Date getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return (Date) this.updatedAt.clone();
    }
    public void setUpdatedAt(final Date updatedAt) {
        if (updatedAt == null) {
            this.updatedAt = null;
        } else {
            this.updatedAt = (Date) updatedAt.clone();
        }
    }
}
