package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件明细信息
 * 项目名称: 微服务运维平台（composite-api 模块）
 * 包: com.migu.tsg.microservice.atomicservice.composite.service.event.dto
 * 类名称: EventDetail.java
 * 类描述: 事件明细信息
 * 创建人: 张庆
 * 创建时间: 2017年10月10日 上午10:45:09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class EventDetail {

    private String actions;

    private String application;

    private String attribute;

    @JsonProperty("attribute_type")
    private String attributeType;

    @JsonProperty("build_config_name")
    private String buildConfigName;

    @JsonProperty("build_config_uuid")
    private String buildConfigUuid;

    private DetailConstraintsDTO constraints;

    private DetailContentDTO content;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("current_version")
    private String currentVersion;

    private String description;

    private String digest;

    private String host;

    private String id;

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("image_tag")
    private String imageTag;

    private String message;

    private String name;

    private String namespace;

    private String operation;

    private String operator;

    private DetailParentDTO parent;

    @JsonProperty("permissionUuid")
    private String permissionUuid;

    private String port;

    private String region;

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("region_uuid")
    private String regionUuid;

    private String registry;

    private String repository;

    private String resource;

    @JsonProperty("resource_actions")
    private String resourceActions;

    private DetailResourcesDTO resources;

    @JsonProperty("role_name")
    private String roleName;

    @SerializedName("role_uuid")
    private String roleUuid;

    private DetailRolesDTO roles;

    private DetailServicesDTO services;

    @JsonProperty("space_name")
    private String spaceName;

    @JsonProperty("space_uuid")
    private String spaceUuid;

    private String status;

    private String template;

    @JsonProperty("template_uuid")
    private String templateUuid;

    private String token;

    @JsonProperty("trigger_type")
    private String triggerType;

    private String type;

    @JsonProperty("unique_name")
    private String uniqueName;

    @JsonProperty("updated_at")
    private String updatedAt;

    private String uuid;

}
