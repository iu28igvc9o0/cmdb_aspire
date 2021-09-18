package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class JobDTO {
	 // 镜像中心uri
    @JsonProperty("registry_index")
    private String registryIndex;

    // 镜像中心名字
    @JsonProperty("registry_name")
    private String registryName;
    
    @JsonProperty("space_name")
    private String spaceName;
    
    @JsonProperty("project_name")
    private String projectName;
    
    @JsonProperty("region_name")
    private String regionName;

    // 镜像中心的项目
    @JsonProperty("registry_project")
    private String registryProject;

    // 镜像名
    @JsonProperty("image_name")
    private String imageName;

    // 镜像标签
    @JsonProperty("image_tag")
    private String imageTag;

    // 资源空间ID
    @JsonProperty("space_uuid")
    private String spaceUuid;

    // 服务所在集群ID
    @JsonProperty("region_id")
    private String regionId;

    // job执行的命令类型
    @JsonProperty("command_type")
    private String commandType;

    // 自定义执行的名令
    private String command;

    // job运行的超时时间
    private Double timeout;

    // 分配的cpu资源
    private Double cpu;

    // 分配的内存资源
    private Integer memory;

    // 任务的创建者
    @JsonProperty("created_by")
    private String createdBy;

    // 项目ID
    @JsonProperty("project_uuid")
    private String projectUuid;

    // 定时规则
    @JsonProperty("schedule_rule")
    private String scheduleRule;
}
