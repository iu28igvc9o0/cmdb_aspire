package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ListJobBaseResponseTemp extends JobDTO{
	 // 主键ID
	@JsonProperty("job_uuid")
    private String jobUuid;

    // 任务状态
    private String status;

    // 环境变量
    private List<EnvvarsDTOCopy> envvars;

    // 环境变量文件
    private List<EnvfilesDTOCopy> envfiles;


  /*  // 创建时间
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createdAt;

    // 任务开始时间
    @JsonProperty("started_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date startedAt;

    // 任务结束时间
    @JsonProperty("end_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date endedAt;*/

    // 任务配置ID
    @JsonProperty("config_uuid")
    private String configUuid;
    // 任务配置名称
    @JsonProperty("config_name")
    private String configName;
    @JsonProperty("resource_actions")
	private List<String> resourceActions;
}
