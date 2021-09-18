package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateJobConfigRequest extends JobDTO {

	// 任务状态
	private String status;
	// 创建时间
	@JsonProperty("created_at")
	private Date createdAt;

	// 任务开始时间
	@JsonProperty("started_at")
	private Date startedAt;

	// 任务结束时间
	@JsonProperty("ended_at")
	private Date endedAt;

	// 环境变量
	private List<EnvvarsDTOCopy> envvars;

	// 环境变量文件
	private List<EnvfilesDTOCopy> envfiles;

	// 任务配置ID
	@JsonProperty("config_uuid")
	private String configUuid;
	// 任务配置名称
	@JsonProperty("config_name")
	private String configName;
	
	/** 定时规则 */
    @JsonProperty("schedule_rule")
    private String scheduleRule;

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
    public Date getStartedAt() {
        if (this.startedAt == null) {
            return null;
        }
        return (Date) this.startedAt.clone();
    }
    public void setStartedAt(final Date startedAt) {
        if (startedAt == null) {
            this.startedAt = null;
        } else {
            this.startedAt = (Date) startedAt.clone();
        }
    }
	public Date getEndedAt() {
        if (this.endedAt == null) {
            return null;
        }
        return (Date) this.endedAt.clone();
    }
    public void setEndedAt(final Date endedAt) {
        if (endedAt == null) {
            this.endedAt = null;
        } else {
            this.endedAt = (Date) endedAt.clone();
        }
    }
}
